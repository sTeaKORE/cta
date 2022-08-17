package tuwien.cta.annotation.test

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CTATestConfig(
    val classToTest: KClass<*>,
    val functionToTest: KFunction<*>,
    val oracle: KFunction<*>
)
