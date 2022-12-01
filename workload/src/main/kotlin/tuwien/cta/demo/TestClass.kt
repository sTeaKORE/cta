package tuwien.cta.demo

import tuwien.cta.annotation.constraint.*

// check latter how to tackle in class enums
enum class Example {
    Example1,
    Example2
}

@CTAConstraints(
    CTAIfConstraint("test"),
    CTAIfConstraint("test"),
    CTAIfConstraint("test"),
    CTAIfConstraint("test"),
    CTAIfConstraint("test"),
    CTAIfConstraint("test"),
)
class TestClass {

    @CTAInt(from = 0, to = 10)
    var numberWithRange: Int = 1

    @CTAInt(values = [0, 1, 2, 3, 4, 5])
    var numberWithArrayOfValues: Int = 3

    @CTABoolean
    var boolean: Boolean = false

    @CTAEnum
    var enum: Example = Example.Example1

    @CTAEnum(values = ["RED", "GREEN", "YELLOW"])
    var stringEnum: String = "RED"

    override fun toString(): String {
        return "numberWithRange: $numberWithRange, numberWithArrayOfValues: $numberWithArrayOfValues, boolean: $boolean, enum: $enum, stringEnum: $stringEnum"
    }

}
