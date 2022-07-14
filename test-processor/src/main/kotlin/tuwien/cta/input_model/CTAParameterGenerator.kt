package tuwien.cta.input_model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tuwien.cta.exception.InvalidAnnotationException

class CTAParameterGenerator {

    fun generateIntParam(annotation: KSAnnotation, name: String): CTAAbstractParameter {
        val valuesArgument = annotation.arguments.find { it.name?.asString() == INT_VALUES }
        val fromArgument = annotation.arguments.find { it.name?.asString() == INT_FROM }
        val toArgument = annotation.arguments.find { it.name?.asString() == INT_TO }
        return if (valuesArgument != null && fromArgument != null && toArgument != null) {
            val valuesList = (valuesArgument.value as List<*>).filterIsInstance<Int>()
            if (valuesList.isNotEmpty()) {
                CTAIntParameter(name, valuesList)
            } else {
                val from = fromArgument.value as Int
                val to = toArgument.value as Int
                CTARangeParameter(name, from, to)
            }
        } else {
            throw InvalidAnnotationException("missing argument on annotation")
        }
    }

    fun generateBooleanParam(name: String): CTAAbstractParameter {
        return CTABooleanParameter(name)
    }

    fun generateEnumParam(annotation: KSAnnotation, name: String): CTAAbstractParameter {
        val annotationParentNode = annotation.parent as KSPropertyDeclaration
        val annotationParentNodeType = annotationParentNode.type.resolve().declaration as KSClassDeclaration
        val enumValues = annotationParentNodeType.declarations.toList().filterIsInstance<KSClassDeclaration>().map { it.simpleName.asString() }
        return CTAEnumParameter(name, enumValues)
    }

    fun generateStringEnum(annotation: KSAnnotation, name: String): CTAAbstractParameter {
        val valuesArgument = annotation.arguments.find { it.name?.asString() == ENUM_VALUES }
        if (valuesArgument != null) {
            val valuesList = (valuesArgument.value as List<*>).filterIsInstance<String>()
            if (valuesList.isNotEmpty()) {
                return CTAEnumParameter(name, valuesList)
            }
        }
        throw InvalidAnnotationException("missing argument on annotation")
    }

    companion object {
        private const val INT_VALUES = "values"
        private const val INT_FROM = "from"
        private const val INT_TO = "to"
        private const val ENUM_VALUES = "values"
    }
}
