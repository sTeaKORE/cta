package tuwien.cta.demo

import org.junit.jupiter.api.Test
import tuwien.cta.annotation.test.CTATest
import tuwien.cta.annotation.test.CTATestContainer
import kotlin.reflect.KFunction

class ATest {

    @Test
    fun testingClass() {
        println("success")

    }

    @CTATest(CTATestContainerImpl::class, TestClass::class)
    fun testingTestClass() {}
}

class CTATestContainerImpl: CTATestContainer {

    override var testMethod: KFunction<*> = FunctionClass()::testingFunction

    override fun oracle(output: String): Boolean {
        return true
    }
}
