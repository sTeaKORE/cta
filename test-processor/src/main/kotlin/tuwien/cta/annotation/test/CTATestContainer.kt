package tuwien.cta.annotation.test

import kotlin.reflect.KFunction

interface CTATestContainer {

    abstract var testMethod: KFunction<*>

    abstract fun oracle(output: String): Boolean
}
