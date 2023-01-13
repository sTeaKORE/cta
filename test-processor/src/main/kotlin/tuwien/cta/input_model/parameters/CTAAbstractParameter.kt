package tuwien.cta.input_model.parameters

/**
 * Abstract Value Constraint Parameter Class.
 * Holds all properties shared among all Value Constraint Parameters.
 * Each constraint has a name as a string.
 */
abstract class CTAAbstractParameter(private val name: String) {

    /**
     * Function which returns parameter in ACTS format for the ACTS config file.
     */
    abstract fun getACTSString(): String

    /**
     * Function which returns parameter as variable for generated test file (.kt file)
     */
    abstract fun getVariableString(): String

    /**
     * @return name of this constraint.
     */
    fun getParameterName(): String {
        return name
    }
}
