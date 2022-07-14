package tuwien.cta.demo

fun main() {
    val builder = AClassBuilder()
    builder
        .withA(1)
        .withB("foo")
        .withC(2.3)
        .withD("hello")
    val aClass : AClass = builder.build()
    println(aClass.foo())
}
