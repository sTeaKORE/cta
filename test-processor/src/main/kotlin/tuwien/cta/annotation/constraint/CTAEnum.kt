package tuwien.cta.annotation.constraint

/**
 * Enum value constraint annotation.
 * This annotation defines a value as an enum.
 * This can either annotate an enum where possible values are automatically derived by the annotated enum or
 * this can annotate a String and define custom enum values with the values array.
 */
annotation class CTAEnum(
    val values: Array<String> = []
)
