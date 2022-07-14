package tuwien.cta.util

import java.io.OutputStream

class LoggingUtil(private var logFile: OutputStream) {

    fun log(input: String) {
        logFile.appendText("$input\n")
    }

    fun close() {
        logFile.close()
    }
}
