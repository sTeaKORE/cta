package tuwien.cta.input_model

abstract class CTAAbstractParameter(private val name: String) {

    abstract fun getACTSString(): String
}
