package tuwien.cta.input_model

class CTABooleanParameter(private val name: String): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(boolean): true, false"
    }

    override fun getVariableString(): String {
        return "$name: $BOOLEAN_IDENTIFIER"
    }

    override fun toString(): String {
        return "Boolean Parameter $name"
    }

    companion object {
        const val BOOLEAN_IDENTIFIER = "Boolean"
    }
}
