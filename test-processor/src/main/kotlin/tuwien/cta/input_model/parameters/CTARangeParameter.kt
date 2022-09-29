package tuwien.cta.input_model.parameters

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
