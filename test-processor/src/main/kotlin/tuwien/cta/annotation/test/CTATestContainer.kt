package tuwien.cta.annotation.test

import kotlin.reflect.KFunction

interface CTATestContainer {

    var testMethod: KFunction<*>

    fun oracle(output: String): Boolean
}
