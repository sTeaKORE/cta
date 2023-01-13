package tuwien.cta.input_model.parameters

/**
 * String Enum Value Constraint Parameter Class.
 * Implements CTAAbstractParameter and all its parameters.
 * Has some additional parameters needed.
 * @param values list of possible values for string enums
 */
class CTAStringEnumParameter(private val name: String, private val values: List<String>): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(enum): ${values.joinToString()}"
    }

    override fun getVariableString(): String {
        return "$name: $STRING_IDENTIFIER"
    }

    override fun toString(): String {
        return "Enum Parameter $name [${values.joinToString()}]"
    }

    companion object {
        const val STRING_IDENTIFIER = "String"
    }
}
