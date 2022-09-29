package tuwien.cta.util

import com.google.devtools.ksp.symbol.KSAnnotation
import tuwien.cta.annotation.constraint.*
import tuwien.cta.exception.InvalidDataTypeException
import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.input_model.CTAParameterGenerator
import java.io.OutputStream
import kotlin.reflect.KClass

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

fun KSAnnotation.validateAnnotatedType(type: CTAType): Boolean {
    return when(type) {
        CTAType.Int -> this.shortName.asString() == CTAInt::class.classNameToString()
        CTAType.Boolean -> this.shortName.asString() == CTABoolean::class.classNameToString()
        CTAType.Enum, CTAType.String -> this.shortName.asString() == CTAEnum::class.classNameToString()
        CTAType.UNKNOWN -> false
    }
}

fun KClass<*>.classNameToString() = this.simpleName.toString()

private val ctaParameterGenerator = CTAParameterGenerator()

fun KSAnnotation.generateParameter(type:CTAType, name: String): CTAAbstractParameter {
    return when(type) {
        CTAType.Int -> ctaParameterGenerator.generateIntParam(this, name)
        CTAType.Boolean -> ctaParameterGenerator.generateBooleanParam(name)
        CTAType.Enum -> ctaParameterGenerator.generateEnumParam(this, name)
        CTAType.String -> ctaParameterGenerator.generateStringEnum(this, name)
        CTAType.UNKNOWN -> throw InvalidDataTypeException("Cannot generate Parameter from type Unknown")
    }
}


