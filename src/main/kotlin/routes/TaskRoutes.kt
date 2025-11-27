package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import model.Task
import storage.TaskStore
import utils.Page
import utils.SessionData
import utils.renderTemplate
import utils.Logger
import utils.RequestIdKey
import utils.newReqId
import utils.jsMode
import utils.timed
import utils.isHtmx

/**
 * Week 6 Lab 1: Simple task routes with HTMX progressive enhancement.
 * Week 9: Instrumentation added.
 */

fun Route.taskRoutes(store: TaskStore = TaskStore()) {

    /**
     * GET /tasks - List all tasks (T1_filter)
     */
    get("/tasks") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val jsMode = call.jsMode()

        // Log query param presence as filter step
        val query = call.request.queryParameters["q"].orEmpty()
        val taskCode = if (query.isNotEmpty()) "T1_filter" else "T0_list"

        call.timed(taskCode = taskCode, jsMode = jsMode) {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val error = call.request.queryParameters["error"]
            val msg = call.request.queryParameters["msg"]
            
            val data = paginateTasks(store, query, page)

            val model = mapOf(
                "title" to "Tasks",
                "page" to data,
                "query" to query,
                "error" to error,
                "msg" to msg
            )
            val html = call.renderTemplate("tasks/index.peb", model)
            call.respondText(html, ContentType.Text.Html)
        }
    }

    /**
     * GET /tasks/fragment - HTMX-only route for filtering/pagination (T1_filter)
     */
    get("/tasks/fragment") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val jsMode = "on" // Always HTMX here

        call.timed(taskCode = "T1_filter", jsMode = jsMode) {
            val query = call.request.queryParameters["q"].orEmpty()
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val data = paginateTasks(store, query, page)

            val model = mapOf(
                "page" to data,
                "query" to query
            )
            val fragment = call.renderTemplate("tasks/_fragment.peb", model)
            val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Found ${data.totalItems} tasks.</div>"""
            call.respondText(fragment + status, ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks - Add new task (T3_add)
     */
    post("/tasks") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val session = call.request.cookies["sid"] ?: "anon"
        val jsMode = call.jsMode()

        call.timed(taskCode = "T3_add", jsMode = jsMode) {
            val title = call.receiveParameters()["title"].orEmpty().trim()

            // Validation
            if (title.isBlank()) {
                Logger.validationError(session, reqId, "T3_add", "blank_title", 0, jsMode)
                if (call.isHtmx()) {
                    val status = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive" class="error-summary">Title is required.</div>"""
                    return@timed call.respondText(status, ContentType.Text.Html, HttpStatusCode.OK)
                } else {
                    return@timed call.respondRedirect("/tasks?error=title")
                }
            }

            if (title.length > 200) {
                Logger.validationError(session, reqId, "T3_add", "max_length", 0, jsMode)
                if (call.isHtmx()) {
                    val status = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive" class="error-summary">Title too long (max 200 chars).</div>"""
                    return@timed call.respondText(status, ContentType.Text.Html, HttpStatusCode.OK)
                } else {
                    return@timed call.respondRedirect("/tasks?error=title&msg=too_long")
                }
            }

            // Success path
            val task = Task(title = title)
            store.add(task)

            if (call.isHtmx()) {
                val itemHtml = call.renderTemplate("tasks/_item.peb", mapOf("task" to task))
                val allTasks = store.getAll()
                val countUpdate = """<h2 id="list-heading" hx-swap-oob="true">Current tasks (${allTasks.size})</h2>"""
                val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Task "${task.title}" added successfully.</div>"""
                val removeNoTasks = """<li id="no-tasks" hx-swap-oob="delete"></li>"""
                call.respondText(itemHtml + countUpdate + status + removeNoTasks, ContentType.Text.Html, HttpStatusCode.Created)
            } else {
                call.respondRedirect("/tasks")
            }
        }
    }

    /**
     * DELETE /tasks/{id} - HTMX delete (T4_delete)
     */
    delete("/tasks/{id}") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val jsMode = "on"

        call.timed(taskCode = "T4_delete", jsMode = jsMode) {
            val id = call.parameters["id"]
            val task = id?.let { store.getById(it) }
            val removed = id?.let { store.delete(it) } ?: false

            if (removed) {
                val message = "Deleted \"${task?.title ?: "task"}\"."
                val remainingTasks = store.getAll()
                val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">$message</div>"""
                val countUpdate = """<h2 id="list-heading" hx-swap-oob="true">Current tasks (${remainingTasks.size})</h2>"""
                
                val listUpdate = if (remainingTasks.isEmpty()) {
                     """<ul id="task-list" hx-swap-oob="true"><li id="no-tasks">No tasks yet. Add one above!</li></ul>"""
                } else { "" }
                
                val mainContent = "<!-- deleted -->"
                call.respondText(status + countUpdate + listUpdate + mainContent, ContentType.Text.Html)
            } else {
                 call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
    }

    /**
     * POST /tasks/{id}/delete - No-JS delete (T4_delete)
     */
    post("/tasks/{id}/delete") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val jsMode = "off"

        call.timed(taskCode = "T4_delete", jsMode = jsMode) {
            val id = call.parameters["id"]
            val removed = id?.let { store.delete(it) } ?: false
            
            call.response.headers.append("Location", "/tasks")
            call.respond(HttpStatusCode.SeeOther)
        }
    }

    /**
     * GET /tasks/{id}/edit - Show edit form
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        val errorParam = call.request.queryParameters["error"]
        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmx()) {
            val html = call.renderTemplate("tasks/_edit.peb", mapOf("task" to task, "error" to errorMessage))
            call.respondText(html, ContentType.Text.Html)
        } else {
            val model = mapOf(
                "title" to "Tasks",
                "tasks" to store.getAll(),
                "editingId" to id,
                "errorMessage" to errorMessage,
            )
            val html = call.renderTemplate("tasks/index.peb", model)
            call.respondText(html, ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks/{id}/edit - Save edits (T2_edit)
     */
    post("/tasks/{id}/edit") {
        val reqId = newReqId()
        call.attributes.put(RequestIdKey, reqId)
        val session = call.request.cookies["sid"] ?: "anon"
        val jsMode = call.jsMode()

        call.timed(taskCode = "T2_edit", jsMode = jsMode) {
            val id = call.parameters["id"] ?: return@timed call.respond(HttpStatusCode.NotFound)
            val task = store.getById(id) ?: return@timed call.respond(HttpStatusCode.NotFound)
            val newTitle = call.receiveParameters()["title"].orEmpty().trim()

            // Validation
            if (newTitle.isBlank()) {
                Logger.validationError(session, reqId, "T2_edit", "blank_title", 0, jsMode)
                if (call.isHtmx()) {
                    val html = call.renderTemplate("tasks/_edit.peb", mapOf(
                        "task" to task,
                        "error" to "Title is required. Please enter at least one character."
                    ))
                    return@timed call.respondText(html, ContentType.Text.Html, HttpStatusCode.BadRequest)
                } else {
                    return@timed call.respondRedirect("/tasks/${id}/edit?error=blank")
                }
            }

            val updatedTask = task.copy(title = newTitle)
            store.update(updatedTask)

            if (call.isHtmx()) {
                val viewHtml = call.renderTemplate("tasks/_item.peb", mapOf("task" to updatedTask))
                val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Task "${updatedTask.title}" updated successfully.</div>"""
                call.respondText(viewHtml + status, ContentType.Text.Html)
            } else {
                call.respondRedirect("/tasks")
            }
        }
    }

    /**
     * GET /tasks/{id}/view - Cancel edit
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        val html = call.renderTemplate("tasks/_item.peb", mapOf("task" to task))
        call.respondText(html, ContentType.Text.Html)
    }
}

/**
 * Helper to paginate and map tasks for template rendering.
 */
private fun paginateTasks(
    store: TaskStore,
    query: String,
    page: Int
): Page<Map<String, Any>> {
    val tasks = store.search(query).map { it.toPebbleContext() }
    return Page.paginate(tasks, currentPage = page, pageSize = 10)
}
