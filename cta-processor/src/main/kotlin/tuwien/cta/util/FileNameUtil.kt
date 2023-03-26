package tuwien.cta.util

const val CONFIG_FILE_SUFFIX = "ACTSConfig"
const val OUTPUT_FILE_SUFFIX = "CAGENTestset"
const val TEST_FILE_SUFFIX = "CTTest"
const val TXT_ENDING = "txt"
const val CSV_ENDING = "csv"

private const val CT_LIBRARY = "fipo-cli"

/**
 * file name helper, used to derive config and output file names depending on the class name
 *
 * @property name name of the file
 * @property packageName package of the file
 * @constructor Create empty file name helper
 */
class CTAFileName(
    private val name: String,
    private val packageName: String,
) {
    /**
     * returns a ACTS config file name with no extension (.txt)
     *
     * @return config file name
     */
    fun getConfigFileNameNoExtension(): String {
        return "$name$CONFIG_FILE_SUFFIX"
    }

    /**
     * returns a ACTS config file name with extension (.txt)
     *
     * @return config file name
     */
    fun getConfigFileNameWithExtension(): String {
        return "$name$CONFIG_FILE_SUFFIX.$TXT_ENDING"
    }

    /**
     * returns name of the auto generated test file
     *
     * @return test file name
     */
    fun getTestFileName(): String {
        return "$name$TEST_FILE_SUFFIX"
    }

    fun getTestFileNamePart(part: Int): String {
        return "$name$TEST_FILE_SUFFIX$part"
    }

    fun getPackage(): String {
        return packageName
    }

    fun getFileName(): String {
        return name
    }
}

/**
 * helper function which derives the test set output name by altering the config name
 * replacing CONFIG_FILE_SUFFIX with OUTPUT_FILE_SUFFIX and .txt with .csv
 *
 * @return output file path + name
 */
fun String.generateOutputFilePath(): String = this.reversed().replaceFirst(
    CONFIG_FILE_SUFFIX.reversed(),
    OUTPUT_FILE_SUFFIX.reversed()
).replaceFirst(
    TXT_ENDING.reversed(),
    CSV_ENDING.reversed()
).reversed()

fun getLibraryName(): String = CT_LIBRARY
