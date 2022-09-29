package tuwien.cta.demo

import org.junit.jupiter.api.Test
import tuwien.cta.annotation.test.CTATest
import tuwien.cta.annotation.test.CTATestContainer
import tuwien.cta.annotation.utility.CTADebug
import kotlin.reflect.KFunction

@CTADebug
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
        println("oracle called")
        return true
    }
}
