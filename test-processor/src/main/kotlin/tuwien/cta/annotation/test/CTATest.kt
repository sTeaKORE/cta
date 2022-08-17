package tuwien.cta.annotation.test

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

@Target(allowedTargets = [AnnotationTarget.PROPERTY])
annotation class CTATest()
