package tuwien.cta.template

import tuwien.cta.testset_generation.CTATestset
import tuwien.cta.util.CTAFileName

/**
 * helper class to hold all relevant information for automatic generated test file
 *
 * @property testSet generated test set from combinatorial testing library
 * @property imports all needed imports
 * @property className name of class of test file
 * @property containerClass container class (from annotation)
 * @property classesToTest classes serving as input (from annotation)
 * @constructor Creates a Test Template Source with given information
 */
class TestTemplateSource(
    val testSet: CTATestset,
    val imports: TestTemplateImports,
    val className: CTAFileName,
    val containerClass: String,
    val classesToTest: List<String>
) {

    /**
     * Get all parameters as variables in a single string.
     *
     * @return string containing all parameters as kotlin variables
     */
    fun getParameterString(): String {
        return testSet.parameters.joinToString() { it.getVariableString() }
    }

    /**
     * function which creates a helper block in the test file which initializes the input class for a test case.
     *
     * @param spacing spacing only used for formatting in template
     * @return helper block as single string
     */
    fun getInitializingFunction(spacing: String): String {
        val stringBuilder = StringBuilder()
        val classToTest = classesToTest[0]
        stringBuilder.append("${spacing}val input = $classToTest()\n")
        testSet.parameters.forEach {
            stringBuilder.append("${spacing}input.${it.getParameterName()} = ${it.getParameterName()}\n")
        }
        stringBuilder.append("${spacing}return input")
        return stringBuilder.toString()
    }
}
