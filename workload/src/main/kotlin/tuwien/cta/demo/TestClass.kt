package tuwien.cta.demo

import tuwien.cta.annotation.constraint.CTAInt
import tuwien.cta.annotation.constraint.CTABoolean
import tuwien.cta.annotation.constraint.CTAEnum


class TestClass {

    enum class Example {
        Example1,
        Example2
    }

    @CTAInt(from = 0, to = 10)
    val numberWithRange: Int = 1

    @CTAInt(values = [0,1,2,3,4,5])
    val numberWithArrayOfValues: Int = 3

    @CTABoolean
    val boolean: Boolean = false

    @CTAEnum
    val enum: Example = Example.Example1

    @CTAEnum(values = ["RED", "GREEN", "YELLOW"])
    val stringEnum: String = "RED"
}
