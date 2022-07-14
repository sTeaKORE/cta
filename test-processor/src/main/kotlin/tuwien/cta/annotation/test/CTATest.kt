package tuwien.cta.annotation.test

import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class CTATest(val testingClass: KClass<*>)
