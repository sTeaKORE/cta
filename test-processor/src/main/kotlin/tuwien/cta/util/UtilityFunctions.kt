package tuwien.cta.util

import com.google.devtools.ksp.symbol.KSAnnotation
import tuwien.cta.annotation.constraint.*
import tuwien.cta.exception.InvalidDataTypeException
import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.input_model.CTAParameterGenerator
import java.io.OutputStream
import kotlin.reflect.KClass

/**
 * helper function for logging
 *
 * @param str string to log
 */
fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

/**
 * validation function to check if annotation has known type and if type and annotation matches
 *
 * @param type type of annotation
 * @return true if known type and type matches annotation, otherwise false
 */
fun KSAnnotation.validateAnnotatedType(type: CTAType): Boolean {
    return when(type) {
        CTAType.Int -> this.shortName.asString() == CTAInt::class.classNameToString()
        CTAType.Boolean -> this.shortName.asString() == CTABoolean::class.classNameToString()
        CTAType.Enum, CTAType.String -> this.shortName.asString() == CTAEnum::class.classNameToString()
        CTAType.UNKNOWN -> false
    }
}

/**
 * small helper to have a single function to get the name of a class
 *
 */
fun KClass<*>.classNameToString() = this.simpleName.toString()

private val ctaParameterGenerator = CTAParameterGenerator()

/**
 * extension function for ksp annotation to generate a parameter from given annotation
 *
 * @param type type given in annotation
 * @param name name of the parameter
 * @return parsed parameter
 * @throws InvalidDataTypeException if type is unknown
 */
fun KSAnnotation.generateParameter(type:CTAType, name: String): CTAAbstractParameter {
    return when(type) {
        CTAType.Int -> ctaParameterGenerator.generateIntParam(this, name)
        CTAType.Boolean -> ctaParameterGenerator.generateBooleanParam(name)
        CTAType.Enum -> ctaParameterGenerator.generateEnumParam(this, name)
        CTAType.String -> ctaParameterGenerator.generateStringEnum(this, name)
        CTAType.UNKNOWN -> throw InvalidDataTypeException("Cannot generate Parameter from type Unknown")
    }
}


