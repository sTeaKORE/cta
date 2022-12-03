package tuwien.cta.input_model

import tuwien.cta.input_model.constraints.CTAConstraint
import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.util.CTAFileName

class CTAInputModel(val fileName: CTAFileName) {

    private val parameters: MutableList<CTAAbstractParameter> = mutableListOf()
    private val constraints: MutableList<CTAConstraint> = mutableListOf()


    private var parameterType: String = ""
    private var name: String = ""

    fun addParameter(parameter: CTAAbstractParameter) {
        parameters.add(parameter)
    }

    fun getParameters(): List<CTAAbstractParameter> {
        return parameters
    }

    fun addConstraint(constraint: CTAConstraint) {
        constraints.add(constraint)
    }

    fun setPayload(parameterType: String, name: String) {
        this.parameterType = parameterType
        this.name = name
    }

    fun getPayload(): Pair<String, String> {
        val parameterType = this.parameterType
        this.parameterType = ""
        val name = this.name
        this.name = ""
        return Pair(parameterType, name)
    }

    fun getParametersString(): String {
        val parameterBuilder = StringBuilder()
        for (parameter in parameters) {
            parameterBuilder.append(parameter.getACTSString())
            parameterBuilder.append("\n")
        }
        return parameterBuilder.toString()
    }

    fun getConstraintsString(): String {
        val constraintBuilder = StringBuilder()
        for (constraint in constraints) {
            constraintBuilder.append(constraint.value)
            constraintBuilder.append("\n")
        }
        return constraintBuilder.toString()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Property Constraints:\n")
        for (propertyConstraint in parameters) {
            stringBuilder.append("$propertyConstraint\n")
        }
        stringBuilder.append("If Constraints:\n")
        for (ifConstraint in constraints) {
            stringBuilder.append("$ifConstraint\n")
        }
        return stringBuilder.toString()
    }
}
