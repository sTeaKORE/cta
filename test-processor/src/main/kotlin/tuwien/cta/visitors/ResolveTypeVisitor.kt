package tuwien.cta.visitors

import com.google.devtools.ksp.symbol.*

class ResolveTypeVisitor : KSVisitor<Unit, String> {

    override fun visitAnnotated(annotated: KSAnnotated, data: Unit): String {
        //not used
        return ""
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: Unit): String {
        //not used
        return ""
    }

    override fun visitCallableReference(reference: KSCallableReference, data: Unit): String {
        //not used
        return ""
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit): String {
        return when(classDeclaration.classKind) {
            ClassKind.ENUM_CLASS -> "Enum"
            ClassKind.CLASS -> classDeclaration.simpleName.asString()
            ClassKind.ANNOTATION_CLASS -> "Annotation"
            else -> "UNKNOWN"
        }
    }

    override fun visitClassifierReference(reference: KSClassifierReference, data: Unit): String {
        //not used
        return ""
    }

    override fun visitDeclaration(declaration: KSDeclaration, data: Unit): String {
        //not used
        return ""
    }

    override fun visitDeclarationContainer(declarationContainer: KSDeclarationContainer, data: Unit): String {
        //not used
        return ""
    }

    override fun visitDynamicReference(reference: KSDynamicReference, data: Unit): String {
        //not used
        return ""
    }

    override fun visitFile(file: KSFile, data: Unit): String {
        //not used
        return ""
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): String {
        //not used
        return ""
    }

    override fun visitModifierListOwner(modifierListOwner: KSModifierListOwner, data: Unit): String {
        //not used
        return ""
    }

    override fun visitNode(node: KSNode, data: Unit): String {
        //not used
        return ""
    }

    override fun visitParenthesizedReference(reference: KSParenthesizedReference, data: Unit): String {
        //not used
        return ""
    }

    override fun visitPropertyAccessor(accessor: KSPropertyAccessor, data: Unit): String {
        //not used
        return ""
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit): String {
        //not used
        return ""
    }

    override fun visitPropertyGetter(getter: KSPropertyGetter, data: Unit): String {
        //not used
        return ""
    }

    override fun visitPropertySetter(setter: KSPropertySetter, data: Unit): String {
        //not used
        return ""
    }

    override fun visitReferenceElement(element: KSReferenceElement, data: Unit): String {
        //not used
        return ""
    }

    override fun visitTypeAlias(typeAlias: KSTypeAlias, data: Unit): String {
        //not used
        return ""
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit): String {
        //not used
        return ""
    }

    override fun visitTypeParameter(typeParameter: KSTypeParameter, data: Unit): String {
        //not used
        return ""
    }

    override fun visitTypeReference(typeReference: KSTypeReference, data: Unit): String {
        //not used
        return ""
    }

    override fun visitValueArgument(valueArgument: KSValueArgument, data: Unit): String {
        //not used
        return ""
    }

    override fun visitValueParameter(valueParameter: KSValueParameter, data: Unit): String {
        //not used
        return ""
    }
}
