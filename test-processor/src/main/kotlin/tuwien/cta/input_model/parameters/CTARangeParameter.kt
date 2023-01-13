package tuwien.cta.input_model.parameters

/**
 * Int Range Value Constraint Parameter Class.
 * Implements CTAAbstractParameter and all its parameters.
 * Has some additional parameters needed.
 * @param from start of int range
 * @param to end of int range
 */
class CTARangeParameter(private val name: String, private val from: Int, private val to: Int): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        val values = (from..to).toList()
        return "$name(int): ${values.joinToString()}"
    }

    override fun getVariableString(): String {
        return "$name: $INT_IDENTIFIER"
    }

    override fun toString(): String {
        return "Range Parameter $name [$from - $to]"
    }

    companion object {
        const val INT_IDENTIFIER = "Int"
    }
}
