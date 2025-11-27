package utils

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.pebbletemplates.pebble.PebbleEngine
import io.ktor.util.AttributeKey
import java.io.StringWriter

// AttributeKey for storing Pebble engine instance
val PebbleEngineKey = AttributeKey<PebbleEngine>("PebbleEngine")

/**
 * Render a Pebble template to HTML string.
 */
suspend fun ApplicationCall.renderTemplate(
    templateName: String,
    context: Map<String, Any?> = emptyMap(),
): String {
    val engine = application.attributes[PebbleEngineKey]
    val writer = StringWriter()
    val template = engine.getTemplate(templateName)

    // Add global context available to all templates
    val sessionData = sessions.get<SessionData>()
    val enrichedContext =
        context +
            mapOf(
                "sessionId" to (sessionData?.id ?: "anonymous"),
                "isHtmx" to isHtmxRequest(),
            )

    template.evaluate(writer, enrichedContext)
    return writer.toString()
}

/**
 * Check if request is from HTMX (progressive enhancement mode).
 */
fun ApplicationCall.isHtmxRequest(): Boolean = request.headers["HX-Request"] == "true"

