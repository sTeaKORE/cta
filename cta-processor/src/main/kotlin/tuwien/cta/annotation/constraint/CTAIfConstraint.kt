package tuwien.cta.annotation.constraint

/**
 * If Constraint Annotation
 * Holds a singular if constraint as string.
 *
 * Example
 *
 * @CTAConstraints(
 *     CTAIfConstraint("enum $CTEQUALS \"Example1\" $CTIMPLIES stringEnum $CTNOTEQUALS \"RED\""),
 *     CTAIfConstraint("rangeInt $CTEQUALS 2 $CTIMPLIES listInt $CTEQUALS 4"),
 * )
 * class TestClass {}
 */
annotation class CTAIfConstraint(
    val constraint: String
)
