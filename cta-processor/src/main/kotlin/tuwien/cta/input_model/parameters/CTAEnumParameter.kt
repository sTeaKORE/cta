package tuwien.cta.input_model.parameters

/**
 * Enum Value Constraint Parameter Class.
 * Implements CTAAbstractParameter and all its parameters.
 * Has some additional parameters needed.
 * @param values list of possible values of enum
 * @param type enum class name
 * @param packageName package for enum class
 */
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
