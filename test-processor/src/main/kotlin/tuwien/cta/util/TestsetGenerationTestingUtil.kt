package tuwien.cta.util

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tuwien.cta.testset_generation.CTAGeneratorConnector
import java.io.File

const val LIBRARY_FILE_LOCATION = "<add library location>"
const val CONFIG_FILE_LOCATION = "<add config location>"

/**
 * Used for debugging test set generation tool, without triggering complete process.
 *
 */
fun main() {
    val connector = CTAGeneratorConnector()

    val libraryFile = File(LIBRARY_FILE_LOCATION)
    val outputFile = connector.generateTestSet(
        CONFIG_FILE_LOCATION,
        libraryFile
    )

    val rows: List<List<String>> = csvReader().readAll(outputFile)
    println("---------------------------------------------------")
    rows.forEach {
        println(it.joinToString())
        println("---------------------------------------------------")
    }
}
