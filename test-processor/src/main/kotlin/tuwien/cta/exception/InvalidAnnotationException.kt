package tuwien.cta.exception

/**
 * Exception used if annotations are invalid e.g. parameters missing on annotation.
 */
class InvalidAnnotationException(message: String): RuntimeException(message)
