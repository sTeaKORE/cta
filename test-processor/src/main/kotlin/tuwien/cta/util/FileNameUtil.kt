package tuwien.cta.util

import tuwien.cta.input_model.CTAInputModel

const val CONFIG_FILE_SUFFIX = "ACTSConfig"
const val OUTPUT_FILE_SUFFIX = "CAGENTestset"
const val TEST_FILE_SUFFIX = "CTTest"
const val TXT_ENDING = "txt"
const val CSV_ENDING = "csv"

private const val CT_LIBRARY = "fipo-cli"

class CTAFileName(
    private val name: String,
    private val packageName: String,
) {
    fun getConfigFileNameNoExtension(): String {
        return "$name$CONFIG_FILE_SUFFIX"
    }

    fun getConfigFileNameWithExtension(): String {
        return "$name$CONFIG_FILE_SUFFIX.$TXT_ENDING"
    }

    fun getTestFileName(): String {
        return "$name$TEST_FILE_SUFFIX"
    }

    fun getPackage(): String {
        return packageName
    }
}

fun String.generateOutputFilePath(): String = this.reversed().replaceFirst(
    CONFIG_FILE_SUFFIX.reversed(),
    OUTPUT_FILE_SUFFIX.reversed()
).replaceFirst(
    TXT_ENDING.reversed(),
    CSV_ENDING.reversed()
).reversed()

fun getLibraryName(): String = CT_LIBRARY
