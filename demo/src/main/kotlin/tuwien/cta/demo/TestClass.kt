package tuwien.cta.demo

import tuwien.cta.annotation.constraint.*

// check latter how to tackle in class enums
enum class Example {
    Example1, Example2
}

@CTAConstraints(
    CTAIfConstraint("enum $CTEQUALS \"Example1\" $CTIMPLIES stringEnum $CTNOTEQUALS \"RED\""),
    CTAIfConstraint("numberWithRange $CTEQUALS 2 $CTIMPLIES numberWithArrayOfValues $CTEQUALS 4"),
    CTAIfConstraint("numberWithRange $CTEQUALS 6 $CTIMPLIES (enum $CTEQUALS \"Example2\" $CTAND (stringEnum $CTEQUALS \"GREEN\" $CTOR stringEnum $CTEQUALS \"YELLOW\"))")
)
class TestClass {

    @CTAInt(from = 0, to = 500)
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
