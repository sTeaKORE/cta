package tuwien.cta.external

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CTAGeneratorConnector() {

    fun generateTestSet(configPath: String) {
        val command = generateCommand(configPath)
        val directory = getResourceDirectory()

        val response = command.runCommand(directory)
        println(response)
    }

    private fun getResourceDirectory(): File {
        val cliBinary = this::class.java.classLoader.getResource("fipo-cli")
        val cliBinaryFile = File(cliBinary.path)
        return cliBinaryFile.parentFile
    }

    private fun generateCommand(configPath: String): String {
        val binaryPath = this::class.java.classLoader.getResource("fipo-cli")
        val binaryPathFile = File(binaryPath.path)
        val binaryPathFilePath = binaryPathFile.absolutePath
        val outputPath = configPath.getOutputPath()
        return "$binaryPathFilePath -t 3 -i $configPath -o $outputPath --randomize"
    }

    companion object {
        const val CONFIG_FILE_SUFFIX = "ACTSConfig"
        const val OUTPUT_FILE_SUFFIX = "CAGENTestset"
        const val TXT_ENDING = "txt"
        const val CSV_ENDING = "csv"
    }
}

fun main() {
    val connector = CTAGeneratorConnector()
    connector.generateTestSet("/home/steakor/uni/master/projekt/workload/build/generated/ksp/test/resources/TestClassACTSConfig.txt")
}

fun String.runCommand(workingDir: File): String? {
    return try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
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

fun String.getOutputPath(): String = this.reversed().replaceFirst(
    CTAGeneratorConnector.CONFIG_FILE_SUFFIX.reversed(),
    CTAGeneratorConnector.OUTPUT_FILE_SUFFIX.reversed()
).replaceFirst(
    CTAGeneratorConnector.TXT_ENDING.reversed(),
    CTAGeneratorConnector.CSV_ENDING.reversed()
).reversed()
