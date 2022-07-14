package tuwien.cta.input_model

class CTAIntParameter(private val name: String, private val values: List<Int>): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(int): ${values.joinToString()}"
    }

    override fun toString(): String {
        return "Int Parameter $name [${values.joinToString()}]"
    }
}
