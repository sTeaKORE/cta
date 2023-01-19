package tuwien.cta.exception

/**
 * Exception if constraints contain unsupported ACTS operations.
 */
class InvalidOperatorException(operation: String): RuntimeException("If Constraint contains not supported ACTS Operation: $operation")

