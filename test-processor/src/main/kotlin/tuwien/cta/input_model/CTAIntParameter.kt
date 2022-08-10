package tuwien.cta.input_model

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
