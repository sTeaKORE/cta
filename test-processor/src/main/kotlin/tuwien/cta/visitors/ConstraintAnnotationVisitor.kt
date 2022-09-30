package tuwien.cta.visitors


import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import tuwien.cta.annotation.constraint.CTAType
import tuwien.cta.util.LoggingUtil
import tuwien.cta.util.validateAnnotatedType
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.util.generateParameter

class ConstraintAnnotationVisitor(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val loggingUtil: LoggingUtil
): KSVisitor<CTAInputModel, CTAInputModel> {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitClassDeclaration(${classDeclaration.simpleName.asString()}) >>> Scanning Class for annotations")
        classDeclaration.annotations.forEach{ it.accept(this, data) }
        classDeclaration.declarations.forEach { it.accept(this, data) }
        return data
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitFunctionDeclaration(${function.simpleName.asString()}) >>> Scanning Function for annotations")
        function.annotations.forEach { it.accept(this, data) }
        return data
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: CTAInputModel): CTAInputModel {
        loggingUtil.log("$VISITIOR_NAME.visitPropertyDeclaration(${property.simpleName.asString()}) >>> Scanning Property for annotations")
        val typeDeclaration = property.type.resolve().declaration
        val propertyType = typeDeclaration.accept(ResolveTypeVisitor(), Unit)
        data.setPayload(propertyType, property.simpleName.asString())
        property.annotations.forEach { it.accept(this, data) }
        return data
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: CTAInputModel): CTAInputModel {
        val (typePayload, namePayload) = data.getPayload()
//        log("Visiting Annotation - ${annotation.shortName.asString()} - with Type - $typePayload")
        val type: CTAType
        try {
            type = CTAType.valueOf(typePayload)
        } catch (e: Exception) {
//            log("Unsupported Data Type: $typePayload, Skipping Annotation")
            return data
        }
        val validated = annotation.validateAnnotatedType(type)
        return if (validated) {
            val propertyConstraint = annotation.generateParameter(type, namePayload)

//            log("Adding Property Constraint: $propertyConstraint")
            data.addParameter(propertyConstraint)
            annotation.arguments.forEach {
//                log("Annotation Argument: ${it.name?.asString()} - ${it.value?.toString()}")
            }
            data
        } else {
//            log("Invalid Annotation Data Type: ${annotation.shortName.asString()} - $typePayload, Skipping Annotation")
            data
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
    }
}
