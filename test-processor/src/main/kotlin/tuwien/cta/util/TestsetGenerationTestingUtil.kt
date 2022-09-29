package tuwien.cta.util

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tuwien.cta.testset_generation.CTAGeneratorConnector
import java.io.File

const val INPUT_FILE_LOCATION = "<insert input file for generation>"
const val OUTPUT_FILE_LOCATION = "<insert desired destination for output file>"

//used for debugging testset generation tool, without triggering complete process
fun main() {
    val connector = CTAGeneratorConnector()

    val libraryFile = File(INPUT_FILE_LOCATION)
    val outputFile = connector.generateTestSet(
        OUTPUT_FILE_LOCATION,
        libraryFile
    )

    val rows: List<List<String>> = csvReader().readAll(outputFile)
    println("---------------------------------------------------")
    rows.forEach {
        println(it.joinToString())
        println("---------------------------------------------------")
    }
}
