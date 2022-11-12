package tuwien.cta.annotation.constraint

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class CTAConstraints(
    val ifConstraints: Array<CTAIfConstraint>
)
