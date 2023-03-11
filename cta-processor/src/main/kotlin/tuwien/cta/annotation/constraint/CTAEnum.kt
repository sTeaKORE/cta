package tuwien.cta.annotation.constraint

/**
 * Enum value constraint annotation.
 * This annotation defines a value as an enum.
 * This can either annotate an enum where possible values are automatically derived by the annotated enum or
 * this can annotate a String and define custom enum values with the values array.
 *
 * Examples
 * ```
 * @CTAEnum
 * var enum: Example = Example.Example1
 * ```
 * ```
 * @CTAEnum(values = ["RED", "GREEN", "YELLOW"])
 * var stringEnum: String = "RED"
 * ```
 */
annotation class CTAEnum(
    val values: Array<String> = []
)
