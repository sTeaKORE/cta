package tuwien.cta.demo

import org.junit.jupiter.api.Test
import tuwien.cta.annotation.test.CTATest
import tuwien.cta.annotation.test.CTATestConfig

class ATest {

    @Test
    fun testingClass() {
        println("success")

    }

    fun oracle(result: String): Boolean {
        return true
    }

//    @CTATest(ImportantClass::class)
    fun testingImportantClass() {}
}

@CTATest
val firstTest = CTATestConfig(TestClass::class, FunctionClass::testingFunction, ATest::oracle)
