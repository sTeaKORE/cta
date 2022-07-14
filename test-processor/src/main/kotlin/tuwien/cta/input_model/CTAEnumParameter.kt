package tuwien.cta.input_model

class CTAEnumParameter(private val name: String, private val values: List<String>): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(enum): ${values.joinToString()}"
    }

    override fun toString(): String {
        return "Enum Parameter $name [${values.joinToString()}]"
    }
}
