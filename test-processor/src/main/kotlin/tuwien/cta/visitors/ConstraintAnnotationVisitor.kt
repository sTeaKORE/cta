package tuwien.cta.visitors


import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import tuwien.cta.annotation.constraint.CTAType
import tuwien.cta.annotation.constraint.CTNOTSUPPORTED
import tuwien.cta.exception.InvalidOperatorException
import tuwien.cta.util.LoggingUtil
import tuwien.cta.util.validateAnnotatedType
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.input_model.constraints.CTAConstraint
import tuwien.cta.util.generateParameter

/**
 * visitor used for parsing all constraint annotations, after parsing everything it returns a input model
 * the somewhat strange choice of always returning input models and passing input models is due to the visitor pattern
 * this leads to cases where input models are passed or returned without any use.
 *
 * @property codeGenerator code generator from ksp
 * @property resolver resolver from ksp
 * @property loggingUtil logging util
 * @constructor Create empty Constraint annotation visitor
 */
class ConstraintAnnotationVisitor(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val loggingUtil: LoggingUtil
): KSVisitor<CTAInputModel, CTAInputModel> {

    /**
     * visitor function for classes, entry point of visitor
     * searches for all declarations (functions, properties) and annotations and passes them to other visitor functions
     *
     * @param classDeclaration class from ksp
     * @param data input model which is passed between functions
     * @return finalized input model
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitClassDeclaration(${classDeclaration.simpleName.asString()}) >>> Scanning Class for annotations")
        classDeclaration.annotations.forEach{ it.accept(this, data) }
        classDeclaration.declarations.forEach { it.accept(this, data) }
        return data
    }

    /**
     * visitor function for functions, searches for annotations and passes them to other visitor functions
     *
     * @param function function from ksp
     * @param data input model which is passed between functions
     * @return input model (ignored)
     */
    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitFunctionDeclaration(${function.simpleName.asString()}) >>> Scanning Function for annotations")
        function.annotations.forEach { it.accept(this, data) }
        return data
    }

    /**
     * visitor function for properties, parses its type with the type resolving visitor and then searches for annotations
     * and passes them to other visitor functions.
     *
     * @param property property from ksp
     * @param data input model which is passed between functions
     * @return input model (ignored)
     */
    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitPropertyDeclaration(${property.simpleName.asString()}) >>> Scanning Property for annotations")
        val typeDeclaration = property.type.resolve().declaration
        val propertyType = typeDeclaration.accept(ResolveTypeVisitor(), Unit)
        data.setPayload(propertyType, property.simpleName.asString())
        property.annotations.forEach { it.accept(this, data) }
        return data
    }

    /**
     * visitor function for annotations, checks if annotation is a value constraint or if constraint and parses it
     * accordingly, adds the constraint then to the input model. if the annotation is none of the two above nothing
     * is done
     *
     * @param annotation annotation from ksp
     * @param data input model which is passed between functions
     * @return input model (ignored)
     */
    override fun visitAnnotation(annotation: KSAnnotation, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Check annotation")
        return if (annotation.shortName.asString() == CONSTRAINTS_ANNOTATION_NAME) {
            parseConstraints(annotation, data)
        } else if(KNOWN_PROPERTY_ANNOTATIONS.contains(annotation.shortName.asString())) {
            parseValueConstraint(annotation, data)
        } else {
            data
        }
    }

    /**
     * helper function to parse value constraints, parses and validates all relevant parameters and adds a property
     * constraint to the input model if the constraint was valid
     *
     * @param annotation annotation from ksp
     * @param data input model which is passed between functions
     * @return updated input model (ignored)
     */
    private fun parseValueConstraint(annotation: KSAnnotation, data: CTAInputModel): CTAInputModel {
        val (typePayload, namePayload) = data.getPayload()
        loggingUtil.log("$VISITIOR_NAME.parseValueConstraint(${annotation.shortName.asString()}) >>> Parsing Property Constraint")
        val type: CTAType
        try {
            type = CTAType.valueOf(typePayload)
        } catch (e: Exception) {
            loggingUtil.log("$VISITIOR_NAME.parseValueConstraint(${annotation.shortName.asString()}) >>> Unsupported Data Type: $typePayload, Skipping Constraint")
            return data
        }
        val validated = annotation.validateAnnotatedType(type)
        return if (validated) {
            val propertyConstraint = annotation.generateParameter(type, namePayload)

            loggingUtil.log("$VISITIOR_NAME.parseValueConstraint(${annotation.shortName.asString()}) >>> Adding Property Constraint: $propertyConstraint")
            data.addParameter(propertyConstraint)
            data
        } else {
            loggingUtil.log("$VISITIOR_NAME.parseValueConstraint(${annotation.shortName.asString()}) >>> Invalid Annotation Data Type: ${annotation.shortName.asString()} - $typePayload, Skipping Constraint")
            data
        }
    }

    /**
     * helper function to parse if constraints, parses the if constraint and adds it to the input model if valid
     *
     * @param annotation annotation from ksp
     * @param data input model which is passed between functions
     * @return updated input model (ignored)
     */
    private fun parseConstraints(annotation: KSAnnotation, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.parseConstraints(${annotation.shortName.asString()}) >>> Parsing Constraint")
        val ifConstraintArgument = annotation.arguments.find { it.name?.asString() == IF_CONSTRAINTS }
        if (ifConstraintArgument != null) {
            val valuesList = (ifConstraintArgument.value as List<*>).filterIsInstance<KSAnnotation>()
            valuesList.forEach { ifConstraint ->
                loggingUtil.log("$VISITIOR_NAME.parseConstraints(${annotation.shortName.asString()}) >>> Parsing If Constraint")
                val constraint = ifConstraint.arguments.find { it.name?.asString() == IF_CONSTRAINTS_VALUE }
                if (constraint != null) {
                    val value = constraint.value as String
                    loggingUtil.log("$VISITIOR_NAME.parseConstraints(${annotation.shortName.asString()}) >>> Adding Constraint $value")
                    checkForUnsupportedOperations(value)
                    data.addConstraint(CTAConstraint(value))
                }
            }
        }
        return data
    }

    /**
     * helper function which checks if constraints for unsupported ACTS operations, since current library only supports part of the
     * ACTS operations
     *
     * @param ifConstraint if constraint to be checked
     * @throws InvalidOperatorException if unsupported operation is present
     */
    private fun checkForUnsupportedOperations(ifConstraint: String) {
        val constraintSplit = ifConstraint.split(" ")
        val unsupportedOperations = constraintSplit.filter { CTNOTSUPPORTED.contains(it) }.firstOrNull()
        if (unsupportedOperations != null) {
            throw InvalidOperatorException(unsupportedOperations)
        }
    }

    override fun visitAnnotated(annotated: KSAnnotated, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitCallableReference(reference: KSCallableReference, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitClassifierReference(reference: KSClassifierReference, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitDeclaration(declaration: KSDeclaration, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitDeclarationContainer(declarationContainer: KSDeclarationContainer, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitDynamicReference(reference: KSDynamicReference, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitFile(file: KSFile, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitModifierListOwner(modifierListOwner: KSModifierListOwner, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitNode(node: KSNode, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitParenthesizedReference(reference: KSParenthesizedReference, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitPropertyAccessor(accessor: KSPropertyAccessor, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitPropertyGetter(getter: KSPropertyGetter, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitPropertySetter(setter: KSPropertySetter, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitReferenceElement(element: KSReferenceElement, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitTypeAlias(typeAlias: KSTypeAlias, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitTypeParameter(typeParameter: KSTypeParameter, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitTypeReference(typeReference: KSTypeReference, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitValueArgument(valueArgument: KSValueArgument, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: CTAInputModel): CTAInputModel {
        //not needed
        return data
    }

    companion object {
        const val VISITIOR_NAME = "ConstraintAnnotationVisitor"
        const val CONSTRAINTS_ANNOTATION_NAME = "CTAConstraints"
        const val IF_CONSTRAINTS = "ifConstraints"
        const val IF_CONSTRAINTS_VALUE = "constraint"
        val KNOWN_PROPERTY_ANNOTATIONS = listOf("CTABoolean", "CTAEnum", "CTAInt")
    }
}
