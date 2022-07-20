package tuwien.cta.demo

import org.junit.jupiter.api.Test
import tuwien.cta.annotation.test.CTATest


class ATest {

    @Test
    fun testingClass() {
        println("success")
    }

    @CTATest(TestClass::class)
    fun testingTestClass() {}

//    @CTATest(ImportantClass::class)
    fun testingImportantClass() {}
}
