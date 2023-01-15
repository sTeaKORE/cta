package tuwien.cta.util

import java.io.OutputStream

/**
 * wrapper for log file, if no file is present which means no debug annotation was present each log leads to noop
 *
 * @property logFile log file
 * @constructor Create empty Logging util
 */
class LoggingUtil(private var logFile: OutputStream?) {

    /**
     * log given input if file is present
     *
     * @param input log message
     */
    fun log(input: String) {
        logFile?.appendText("$input\n")
    }

    /**
     * Close stream to log file
     *
     */
    fun close() {
        logFile?.close()
    }
}
