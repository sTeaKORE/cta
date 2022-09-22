package tuwien.cta.template

import tuwien.cta.input_model.CTAAbstractParameter
import tuwien.cta.input_model.CTAEnumParameter

class TestTemplateSource(
    val packageName: String,
    val testClassName: String,
    val classToTest: String,
    val testCasesList: List<String>,
    val containerClass: String,
    private val parameters: List<CTAAbstractParameter>
) {
    fun getImport(): List<String> {
        val imports = mutableListOf<String>()
        imports.add("$packageName.$classToTest")
        parameters.filterIsInstance<CTAEnumParameter>().forEach { imports.add(it.getImport()) }
        return imports
    }

    fun getParameterString(): String {
        return parameters.joinToString() { it.getVariableString() }
    }

    fun getInitializingFunction(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("val input = $classToTest()\n")
        parameters.forEach {
            stringBuilder.append("input.${it.getParameterName()} = ${it.getParameterName()}\n")
        }
        stringBuilder.append("return input")
        return stringBuilder.toString()
    }
}
