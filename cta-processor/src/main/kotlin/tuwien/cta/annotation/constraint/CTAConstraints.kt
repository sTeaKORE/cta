package tuwien.cta.annotation.constraint

/**
 * Constraints Annotation.
 * Holds all if constraints for one class as a list.
 *
 * Example
 *
 * @CTAConstraints(
 *     ...
 * )
 * class TestClass {}
 */
@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class CTAConstraints(
    vararg val ifConstraints: CTAIfConstraint
)
