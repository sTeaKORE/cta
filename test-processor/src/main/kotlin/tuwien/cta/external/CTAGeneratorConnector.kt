package tuwien.cta.external

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CTGeneratorConnector() {

    private val command = "./fipo-cli -i 2^10 -p"

    fun generateTestSet() {
        val directory = getResourceDirectory()
        val response = command.runCommand(directory)
        println(response)
    }

    private fun getResourceDirectory(): File {
        val cliBinary = this::class.java.classLoader.getResource("fipo-cli")
        val cliBinaryFile = File(cliBinary.path)
        return cliBinaryFile.parentFile
    }
}

fun main() {
    val connector = CTGeneratorConnector()
    connector.generateTestSet()
}

fun String.runCommand(workingDir: File): String? {
    return try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        null
    }
}
