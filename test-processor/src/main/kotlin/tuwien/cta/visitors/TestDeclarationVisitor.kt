package tuwien.cta.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import tuwien.cta.external.CTAGeneratorConnector
import tuwien.cta.template.createTestTemplate
import tuwien.cta.util.LoggingUtil
import tuwien.cta.util.appendText
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.template.createACTSemplate
import java.io.FileNotFoundException

class TestDeclarationVisitor(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val loggingUtil: LoggingUtil
): KSVisitorVoid() {

    private val knownAnnotations = listOf("CTATest")
    private val generatorConnector = CTAGeneratorConnector()

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        if (!visited.contains(function.simpleName.asString())) {
            visited.add(function.simpleName.asString())
            loggingUtil.log("----- Visiting Function Declaration - ${function.simpleName.asString()}, Searching for Annotations -----")
            function.annotations
                .filter { knownAnnotations.contains(it.shortName.asString()) }
                .forEach { it.accept(this, data) }

            val parent = function.parentDeclaration as KSClassDeclaration
            val packageName = parent.containingFile!!.packageName.asString()
            val className = "${function.simpleName.asString()}Test"
            val file =
                codeGenerator.createNewFile(Dependencies(true, function.containingFile!!), packageName, className)
            val template = createTestTemplate(packageName, className)
            file.appendText("$template\n")
            file.close()
        }
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
        loggingUtil.log("Visiting Annotation - ${annotation.shortName.asString()}")
        for (argument: KSValueArgument in annotation.arguments) {
            loggingUtil.log("Annotation Argument:\n  Name - ${argument.name?.asString()}\n  Value - ${argument.value}")
            val argumentType = argument.value as KSType
            val argumentDeclaration = argumentType.declaration
            loggingUtil.log("Found Declaration (class, function, value) in annotation:\n  " +
                    "Name - ${argumentDeclaration.simpleName.asString()}\n  Package - ${argumentDeclaration.packageName.asString()}")
            if (argumentDeclaration is KSClassDeclaration) {
                loggingUtil.log("Declaration is Class Annotation => accepting Declaration")
                var inputModel = CTAInputModel(argumentDeclaration.simpleName.asString())
                inputModel = argumentDeclaration.accept(ConstraintAnnotationVisitor(codeGenerator, resolver, loggingUtil), inputModel)
                loggingUtil.log("Received Input Model:\n$inputModel")
                val pathToACTSFile = generateACTSFile(inputModel)
                generatorConnector.generateTestSet(pathToACTSFile)
            }
        }
    }

    private fun generateACTSFile(inputModel: CTAInputModel): String {
        val filename = "${inputModel.systemName}${CTAGeneratorConnector.CONFIG_FILE_SUFFIX}"
        val file = codeGenerator.createNewFile(Dependencies(false), "", filename, "txt")

        val actsTemplate = createACTSemplate(inputModel)
        file.appendText(actsTemplate)
        file.close()

        val configFile = codeGenerator.generatedFile.find { it.name == "$filename.txt" }
        if (configFile != null) {
            return configFile.absolutePath
        } else {
            throw FileNotFoundException("Generated Config File not found")
        }
    }

    companion object {
        val visited = mutableListOf<String>()
    }
}
