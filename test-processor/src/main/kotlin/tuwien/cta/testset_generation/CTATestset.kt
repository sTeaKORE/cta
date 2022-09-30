package tuwien.cta.testset_generation

import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.input_model.parameters.CTAEnumParameter
import tuwien.cta.input_model.parameters.CTAStringEnumParameter

class CTATestset(val parameters: List<CTAAbstractParameter>, private val testSet: List<List<String>>) {

    fun getTestSetArguments(): List<String> {
        val testSetArguments = mutableListOf<String>()
        val stringBuilder = StringBuilder()
        testSet.forEach { testSetEntry ->
            testSetEntry.forEachIndexed { index, testSetValue ->
                when (val parameter = parameters[index]) {
                    is CTAEnumParameter -> {
                        stringBuilder.append("${parameter.getEnumPrefix()}$testSetValue, ")
                    }

                    is CTAStringEnumParameter -> {
                        stringBuilder.append("\"$testSetValue\", ")
                    }

                    else -> {
                        stringBuilder.append("$testSetValue, ")
                    }
                }
            }
            stringBuilder.setLength(stringBuilder.length - 2)
            testSetArguments.add(stringBuilder.toString())
            stringBuilder.clear()
        }
        return testSetArguments
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("TestSet:\n\n")
        testSet.forEach { testSetEntry ->
            testSetEntry.forEachIndexed { index, testSetValue ->
                val parameter = parameters[index]
                stringBuilder.append("${parameter.getParameterName()} = $testSetValue  ")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}
