package tuwien.cta.demo

import kotlin.reflect.KFunction

class FunctionClass {

    fun testingFunction(input: TestClass) {
        println("success")
    }
}

class Temp() {

    class TempTemp(val function: KFunction<*>)

    fun temp() {
        val temp = TempTemp(FunctionClass::testingFunction)

    }
}