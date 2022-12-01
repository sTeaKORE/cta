package tuwien.cta.annotation.constraint

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class CTAConstraints(
    vararg val ifConstraints: CTAIfConstraint
)
