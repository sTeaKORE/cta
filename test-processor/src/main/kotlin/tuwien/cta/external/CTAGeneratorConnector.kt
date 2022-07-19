package tuwien.cta.external

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CTAGeneratorConnector() {

    fun generateTestSet(configPath: String, library: File) {
        val (command, outputPath) = generateCommand(configPath, library)
        val directory = getResourceDirectory()

        val response = command.runCommand(directory)
        println(response)

        val outputFile = File(outputPath)
        val outputContent = outputFile.readText()
        println(outputContent)
    }

    private fun getResourceDirectory(): File {
        val cliBinary = this::class.java.classLoader.getResource("fipo-cli")
        val cliBinaryFile = File(cliBinary.path)
        return cliBinaryFile.parentFile
    }

    private fun generateCommand(configPath: String, library: File): Pair<String, String> {
        val binaryPath = this::class.java.classLoader.getResourceAsStream("fipo-cli")
        binaryPath.use { input ->
            library.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        library.setExecutable(true)
        val binaryPathFilePath = library.absolutePath
        val outputPath = configPath.getOutputPath()
        return Pair("$binaryPathFilePath -t 3 -i $configPath -o $outputPath --randomize", outputPath)
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
    val libraryFile = File("/home/steakor/uni/master/projekt/workload/build/generated/ksp/test/resources/fipo-cli2")
    connector.generateTestSet("/home/steakor/uni/master/projekt/workload/build/generated/ksp/test/resources/TestClassACTSConfig.txt", libraryFile)
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
