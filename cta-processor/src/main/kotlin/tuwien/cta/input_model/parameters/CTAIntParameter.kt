package tuwien.cta.input_model.parameters

/**
 * Int Value Constraint Parameter Class.
 * Implements CTAAbstractParameter and all its parameters.
 * Has some additional parameters needed.
 * @param values list of possible int values
 */
class CTAIntParameter(private val name: String, private val values: List<Int>): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(int): ${values.joinToString()}"
    }

    override fun getVariableString(): String {
        return "$name: $INT_IDENTIFIER"
    }

    override fun toString(): String {
        return "Int Parameter $name [${values.joinToString()}]"
    }

    companion object {
        const val INT_IDENTIFIER = "Int"
    }
}
