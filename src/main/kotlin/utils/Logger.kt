package utils

import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Thread-safe CSV logger for evaluation metrics.
 * Follows the schema defined in Week 9 Lab 1.
 */
object Logger {
    private const val LOG_FILE = "data/metrics.csv"
    private val lock = Any()

    init {
        // Initialize file with header if it doesn't exist
        val file = File(LOG_FILE)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText("ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n")
        }
    }

    fun success(session: String, reqId: String, task: String, ms: Long, jsMode: String) {
        write(session, reqId, task, "", "success", ms, 200, jsMode)
    }

    fun validationError(session: String, reqId: String, task: String, step: String, ms: Long, jsMode: String) {
        // Note: HTTP status for validation errors varies (400/200/303), using 400 as generic indicator in logs
        write(session, reqId, task, step, "validation_error", ms, 400, jsMode)
    }

    /**
     * Log an event to metrics.csv
     */
    fun write(
        sessionId: String,
        requestId: String,
        taskCode: String,
        step: String = "",
        outcome: String,
        ms: Long,
        httpStatus: Int,
        jsMode: String
    ) {
        val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        
        // CSV escaping (basic)
        fun escape(s: String) = if (s.contains(",")) "\"$s\"" else s

        val row = listOf(
            timestamp,
            escape(sessionId),
            escape(requestId),
            escape(taskCode),
            escape(step),
            escape(outcome),
            ms.toString(),
            httpStatus.toString(),
            jsMode
        ).joinToString(",")

        synchronized(lock) {
            File(LOG_FILE).appendText("$row\n")
        }
    }
}
