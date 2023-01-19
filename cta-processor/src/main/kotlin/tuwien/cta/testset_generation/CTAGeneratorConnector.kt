package tuwien.cta.testset_generation

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tuwien.cta.util.generateOutputFilePath
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * connector to combinatorial testing library
 *
 * @constructor Create empty connector
 */
class CTAGeneratorConnector() {

    /**
     * connects to the library to generate a test set from given input model config
     *
     * @param configPath path to config in ACTS format
     * @param library library used for generating.
     * @return file containing the output of the library (test set)
     * @throws FileNotFoundException if something went wrong during generation (e.g. a constraint was falsely defined)
     */
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

    /**
     * helper function to generate command line command to execute library
     *
     * @param configPath path to ACTS config
     * @param library library used for generating.
     * @return Pair containing the command and the output directory after generation
     */
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

    /**
     * helper function checking if file is empty
     *
     * @param file file to be checked
     * @return true if file empty, otherwise false
     */
    private fun isFileEmpty(file: File): Boolean {
        return try {
            val br = BufferedReader(FileReader(file))
            br.readLine() == null
        } catch (e: IOException) {
            true
        }
    }
}

/**
 * extension function used to run a command line command which it extends.
 *
 * @return output of library.
 */
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


