package tuwien.cta.testset_generation

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tuwien.cta.util.generateOutputFilePath
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.concurrent.TimeUnit

class CTAGeneratorConnector() {

    fun generateTestSet(configPath: String, library: File): File {
        val (command, outputPath) = generateCommand(configPath, library)

        val response = command.runCommand()

        val outputFile = File(outputPath)
        if (isFileEmpty(outputFile)) {
            throw FileNotFoundException("Generated Testset at location: ${outputFile.absolutePath} not found or empty, Reason: $response")
        } else {
            return outputFile
        }
    }

    private fun generateCommand(configPath: String, library: File): Pair<String, String> {
        if (isFileEmpty(library)) {
            val binaryInputStream = this::class.java.classLoader.getResourceAsStream("fipo-cli")
            binaryInputStream.use { input ->
                library.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            library.setExecutable(true)
        }

        val libraryPath = library.absolutePath
        val outputPath = configPath.generateOutputFilePath()
        val command = "$libraryPath -t 3 -i $configPath -o $outputPath --randomize"
        return Pair(command, outputPath)
    }

    private fun isFileEmpty(file: File): Boolean {
        return try {
            val br = BufferedReader(FileReader(file))
            br.readLine() == null
        } catch (e: IOException) {
            true
        }
    }
}

fun String.runCommand(): String {
    return try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        "${proc.inputStream.bufferedReader().readText()}${proc.errorStream.bufferedReader().readText()}"
    } catch(e: IOException) {
        e.localizedMessage
    }
}


