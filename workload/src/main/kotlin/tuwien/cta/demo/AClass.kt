package tuwien.cta.demo

import tuwien.cta.annotation.Builder

@Builder
class AClass(private val a: Int, val b: String, val c: Double, val d: String) {
    val p = "$a, $b, $c, $d"
    fun foo() = p
}
