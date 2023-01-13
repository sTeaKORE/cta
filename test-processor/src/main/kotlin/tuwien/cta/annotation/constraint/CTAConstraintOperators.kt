package tuwien.cta.annotation.constraint

const val CTAND = "&&"
const val CTOR = "||"
const val CTIMPLIES = "=>"
const val CTNOT = "!"
const val CTEQUALS = "="
const val CTNOTEQUALS = "!="

/**
 * List of unsupported ACTS operations. Currently used library uses ACTS standard but does
 * not support the full functionality
 */
val CTNOTSUPPORTED = listOf(">", "<", ">=", "<=", "+", "-", "*", "/", "%")
