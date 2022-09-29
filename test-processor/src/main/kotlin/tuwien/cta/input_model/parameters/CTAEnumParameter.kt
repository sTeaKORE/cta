package tuwien.cta.input_model.parameters

class CTAEnumParameter(
    private val name: String,
    private val values: List<String>,
    private val type: String,
    private val packageName: String
) : CTAAbstractParameter(name) {

    override fun getACTSString(): String {
        return "$name(enum): ${values.joinToString()}"
    }

    override fun getVariableString(): String {
        return "$name: $type"
    }

    override fun toString(): String {
        return "Enum Parameter $name [${values.joinToString()}]"
    }

    fun getImport(): String {
        return "$packageName.$type"
    }

    fun getEnumPrefix(): String {
        return "$type."
    }
}
