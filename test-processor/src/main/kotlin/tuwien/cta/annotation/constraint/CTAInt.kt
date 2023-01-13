package tuwien.cta.annotation.constraint

/**
 * Integer value constraint annotation.
 * This annotation defines a value as int.
 * Possible Values can either be defined by specifically adding them in a values array or by defining a range with the
 * from and to parameters.
 */
annotation class CTAInt(
    val from: Int = 0,
    val to: Int = 1,
    val values: IntArray = []
)
