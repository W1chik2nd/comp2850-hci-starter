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
import utils.isHtmxRequest

/**
 * Week 6 Lab 1: Simple task routes with HTMX progressive enhancement.
 *
 * **Teaching approach**: Start simple, evolve incrementally
 * - Week 6: Basic CRUD with Int IDs
 * - Week 7: Add toggle, inline edit
 * - Week 8: Add pagination, search
 */

fun Route.taskRoutes(store: TaskStore = TaskStore()) {
    /**
     * GET /tasks - List all tasks
     * Returns full page (no HTMX differentiation in Week 6)
     */
    get("/tasks") {
        val query = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val tasks = store.search(query)
        val data = Page.paginate(tasks.map { it.toPebbleContext() }, page, pageSize = 10)

        val model = mapOf(
            "title" to "Tasks",
            "page" to data,
            "query" to query
        )
        val html = call.renderTemplate("tasks/index.peb", model)
        call.respondText(html, ContentType.Text.Html)
    }

    /**
     * GET /tasks/fragment - HTMX-only route for filtering/pagination
     * Returns only the task list area
     */
    get("/tasks/fragment") {
        val query = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val tasks = store.search(query)
        val data = Page.paginate(tasks.map { it.toPebbleContext() }, page, pageSize = 10)

        val model = mapOf(
            "page" to data,
            "query" to query
        )
        val fragment = call.renderTemplate("tasks/_fragment.peb", model)
        val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Found ${data.totalItems} tasks.</div>"""
        call.respondText(fragment + status, ContentType.Text.Html)
    }

    /**
     * POST /tasks - Add new task
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

        if (title.isBlank()) {
            // Validation error handling
            if (call.isHtmxRequest()) {
                val error = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                    Title is required. Please enter at least one character.
                </div>"""
                return@post call.respondText(error, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS: redirect back (could add error query param)
                call.response.headers.append("Location", "/tasks")
                return@post call.respond(HttpStatusCode.SeeOther)
            }
        }

        val task = Task(title = title)
        store.add(task)

        if (call.isHtmxRequest()) {
            // Return HTML fragment for new task using _item.peb template
            val itemHtml = call.renderTemplate("tasks/_item.peb", mapOf("task" to task))

            // Update task count using OOB swap
            val allTasks = store.getAll()
            val countUpdate = """<h2 id="list-heading" hx-swap-oob="true">Current tasks (${allTasks.size})</h2>"""
            val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Task "${task.title}" added successfully.</div>"""
            
            // Remove "no-tasks" message if it exists
            val removeNoTasks = """<li id="no-tasks" hx-swap-oob="delete"></li>"""

            return@post call.respondText(itemHtml + countUpdate + status + removeNoTasks, ContentType.Text.Html, HttpStatusCode.Created)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     * Dual-mode: HTMX empty response or PRG redirect
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]
        val removed = id?.let { store.delete(it) } ?: false

        if (call.isHtmxRequest()) {
            val message = if (removed) "Task deleted." else "Could not delete task."

            // Update task count using OOB swap
            val remainingTasks = store.getAll()

            // Build response: OOB swaps first, then main content
            val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">$message</div>"""
            val countUpdate = """<h2 id="list-heading" hx-swap-oob="true">Current tasks (${remainingTasks.size})</h2>"""
            
            // If no tasks remaining, show empty state (replace entire list to be safe)
            val listUpdate = if (remainingTasks.isEmpty()) {
                 """<ul id="task-list" hx-swap-oob="true"><li id="no-tasks">No tasks yet. Add one above!</li></ul>"""
            } else {
                ""
            }

            // Main content: empty string for outerHTML swap removes the <li>
            // Use comment to ensure response is not completely empty
            val mainContent = if (removed) "<!-- deleted -->" else "<span>Could not delete task.</span>"

            return@post call.respondText(status + countUpdate + listUpdate + mainContent, ContentType.Text.Html)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    /**
     * GET /tasks/{id}/edit - Show edit form
     * Dual-mode: HTMX fragment or full page with editingId
     * Handles error query parameter for No-JS validation errors
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        // Handle error query parameter (No-JS path)
        val errorParam = call.request.queryParameters["error"]
        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmxRequest()) {
            // HTMX path: return edit fragment
            val html = call.renderTemplate("tasks/_edit.peb", mapOf("task" to task, "error" to errorMessage))
            return@get call.respondText(html, ContentType.Text.Html)
        } else {
            // No-JS path: full-page render with editingId
            val model = mapOf(
                "title" to "Tasks",
                "tasks" to store.getAll(),
                "editingId" to id,
                "errorMessage" to errorMessage,
            )
            val html = call.renderTemplate("tasks/index.peb", model)
            return@get call.respondText(html, ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks/{id}/edit - Save edits with validation
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.NotFound)

        val task = store.getById(id) ?: return@post call.respond(HttpStatusCode.NotFound)

        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation
        if (newTitle.isBlank()) {
            if (call.isHtmxRequest()) {
                // HTMX path: return edit fragment with error
                val html = call.renderTemplate("tasks/_edit.peb", mapOf(
                    "task" to task,
                    "error" to "Title is required. Please enter at least one character."
                ))
                return@post call.respondText(html, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS path: redirect with error flag
                return@post call.respondRedirect("/tasks/${id}/edit?error=blank")
            }
        }

        // Update task
        val updatedTask = task.copy(title = newTitle)
        val success = store.update(updatedTask)

        if (!success) {
            return@post call.respond(HttpStatusCode.NotFound, "Task not found")
        }

        if (call.isHtmxRequest()) {
            // HTMX path: return view fragment + OOB status
            val viewHtml = call.renderTemplate("tasks/_item.peb", mapOf("task" to updatedTask))
            val status = """<div id="status" role="status" aria-live="polite" aria-atomic="true" hx-swap-oob="true">Task "${updatedTask.title}" updated successfully.</div>"""

            return@post call.respondText(viewHtml + status, ContentType.Text.Html)
        }

        // No-JS path: PRG redirect
        call.respondRedirect("/tasks")
    }

    /**
     * GET /tasks/{id}/view - Cancel edit (HTMX only)
     * Returns view fragment to replace edit form
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = store.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        // HTMX only - return view fragment
        val html = call.renderTemplate("tasks/_item.peb", mapOf("task" to task))
        call.respondText(html, ContentType.Text.Html)
    }
}
