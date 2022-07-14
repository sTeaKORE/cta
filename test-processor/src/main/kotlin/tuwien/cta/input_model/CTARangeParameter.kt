package tuwien.cta.input_model

class CTARangeParameter(private val name: String, private val from: Int, private val to: Int): CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        val values = (from..to).toList()
        return "$name(int): ${values.joinToString()}"
    }

    override fun toString(): String {
        return "Range Parameter $name [$from - $to]"
    }
}
