package tuwien.cta.util

import tuwien.cta.input_model.CTAInputModel

const val CONFIG_FILE_SUFFIX = "ACTSConfig"
const val OUTPUT_FILE_SUFFIX = "CAGENTestset"
const val TXT_ENDING = "txt"
const val CSV_ENDING = "csv"

private const val CT_LIBRARY = "fipo-cli"

fun String.generateOutputFilePath(): String = this.reversed().replaceFirst(
    CONFIG_FILE_SUFFIX.reversed(),
    OUTPUT_FILE_SUFFIX.reversed()
).replaceFirst(
    TXT_ENDING.reversed(),
    CSV_ENDING.reversed()
).reversed()

fun CTAInputModel.getFilenameNoExtension() = "${this.systemName}${CONFIG_FILE_SUFFIX}"
fun CTAInputModel.getFilenameWithExtension() = "${this.systemName}${CONFIG_FILE_SUFFIX}.$TXT_ENDING"

fun getLibraryName(): String = CT_LIBRARY
