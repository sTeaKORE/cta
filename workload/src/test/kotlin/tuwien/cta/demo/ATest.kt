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

    override fun oracle(methodToTest: KFunction<*>, inputs: Array<Any>): Boolean {
        //setup
        println("setup before method call")

        //call function
        println("call method")
        methodToTest.call(*inputs)

        //evaluate result
        println("evaluate result")

        //return result
        println("returning result")
        return true
    }
}
