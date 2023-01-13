package tuwien.cta.input_model

import tuwien.cta.input_model.constraints.CTAConstraint
import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.util.CTAFileName

/**
 * Class which represents the input model which is then used as input for the combinatorial testing test set generation.
 * @param fileName File Name of the Input Model, used for generation purposes.
 */
class CTAInputModel(val fileName: CTAFileName) {

    /**
     * List of Value Constraints.
     */
    private val parameters: MutableList<CTAAbstractParameter> = mutableListOf()

    /**
     * List of If Constraints.
     */
    private val constraints: MutableList<CTAConstraint> = mutableListOf()


    /**
     * Values used to transfer data between visitors.
     */
    private var parameterType: String = ""
    /**
     * Values used to transfer data between visitors.
     */
    private var name: String = ""

    /**
     * Adds Value Constraint to Input Model.
     * @param parameter Value Constraint which is added.
     */
    fun addParameter(parameter: CTAAbstractParameter) {
        parameters.add(parameter)
    }

    /**
     * Returns all Value Constraints of Input Model.
     * @return list of value constraints
     */
    fun getParameters(): List<CTAAbstractParameter> {
        return parameters
    }

    /**
     * Adds If Constraint to Input Model.
     * @param constraint If Constraint which is added.
     */
    fun addConstraint(constraint: CTAConstraint) {
        constraints.add(constraint)
    }

    /**
     * Sets temporary payload to pass data between visitor methods.
     * @param parameterType Type of value constraint.
     * @param name Name of value constraint.
     */
    fun setPayload(parameterType: String, name: String) {
        this.parameterType = parameterType
        this.name = name
    }

    /**
     * Retrieves temporarily set payload. Also resets payload.
     * @return Pair of name and type of value constraint.
     */
    fun getPayload(): Pair<String, String> {
        val parameterType = this.parameterType
        this.parameterType = ""
        val name = this.name
        this.name = ""
        return Pair(parameterType, name)
    }

    /**
     * Returns all value constraints of the input model in the ACTS config format.
     * @return value constraints in ACTS format.
     */
    fun getParametersString(): String {
        val parameterBuilder = StringBuilder()
        for (parameter in parameters) {
            parameterBuilder.append(parameter.getACTSString())
            parameterBuilder.append("\n")
        }
        return parameterBuilder.toString()
    }

    /**
     * Returns all if constraints of the input model in the ACTS config format.
     * @return if constraints in ACTS format.
     */
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
