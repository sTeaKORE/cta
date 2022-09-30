package tuwien.cta.template

import tuwien.cta.testset_generation.CTATestset
import tuwien.cta.util.CTAFileName

class TestTemplateSource(
    val testSet: CTATestset,
    val imports: TestTemplateImports,
    val className: CTAFileName,
    val containerClass: String,
    val classesToTest: List<String>
) {

    fun getParameterString(): String {
        return testSet.parameters.joinToString() { it.getVariableString() }
    }

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
