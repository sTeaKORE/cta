package tuwien.cta.annotation.test

import kotlin.reflect.KFunction

interface CTATestContainer {

    var testMethod: KFunction<*>

    fun oracle(methodToTest: KFunction<*>, inputs: Array<Any>): Boolean
}
