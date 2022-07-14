package tuwien.cta.input_model

class CTABooleanParameter(private val name: String): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(boolean): true, false"
    }

    override fun toString(): String {
        return "Boolean Parameter $name"
    }
}
