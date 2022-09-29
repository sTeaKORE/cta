package tuwien.cta.visitors

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import tuwien.cta.testset_generation.CTAGeneratorConnector
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.util.*
import java.lang.Exception

class TestDeclarationVisitor(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val loggingUtil: LoggingUtil
) : KSVisitorVoid() {

    private val knownAnnotations = listOf("CTATest")
    private val generatorConnector = CTAGeneratorConnector()
    private val fileUtil = FileUtil(codeGenerator)

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        val functionName = function.simpleName.asString()
        if (visited.contains(functionName)) {
            return
        }
        visited.add(functionName)

        loggingUtil.log("$VISITIOR_NAME.visitFunctionDeclaration($functionName) >>> Searching for ${knownAnnotations.joinToString()} annotations")
        function.annotations
            .filter { knownAnnotations.contains(it.shortName.asString()) }
            .forEach { it.accept(this, data) }
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Processing Annotation")

        val containerClass = extractContainerClass(annotation) ?: return
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Successfully extracted Container Class $containerClass")


        val inputModelClasses = extractInputModelClasses(annotation)
        if (inputModelClasses.isEmpty()) {
            loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> No Classes found for Input Model")
            return
        }

        var inputModel = CTAInputModel(inputModelClasses[0].simpleName.asString())
        for (inputModelClass in inputModelClasses) {
            loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Processing Class Declaration ${inputModelClass.packageName}.${inputModelClass.simpleName.asString()} into Input Model")
            inputModel = inputModelClass.accept(
                ConstraintAnnotationVisitor(codeGenerator, resolver, loggingUtil),
                inputModel
            )
        }

        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Final Input Model:\n$inputModel")
        val pathToACTSFile = fileUtil.generateACTSFile(inputModel)
        val libraryFile = fileUtil.getLibraryFile()
        val testSetFile = generatorConnector.generateTestSet(pathToACTSFile, libraryFile)
        val testSet = csvReader().readAll(testSetFile)
        inputModel.setTestSet(testSet)
        val testSetString = inputModel.getTestsetString()
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Generated Testset:\n$testSetString")
        fileUtil.generateTestFile(inputModel, inputModelClasses[0], containerClass)
    }

    private fun extractContainerClass(annotation: KSAnnotation): String? {
        val container = annotation.arguments.find {
            val argumentName = it.name
            argumentName != null && argumentName.asString() == CTA_CONTAINER_ARGUMENT
        } ?: return null
        val containerType = container.value as KSType
        val containerDeclaration = containerType.declaration
        return if (containerDeclaration is KSClassDeclaration) {
            containerDeclaration.simpleName.asString()
        } else {
            null
        }
    }

    private fun extractInputModelClasses(annotation: KSAnnotation): List<KSClassDeclaration> {
        val inputAnnotations = annotation.arguments.filter {
            val argumentName = it.name
            argumentName != null && argumentName.asString() == CTA_INPUTMODEL_ARGUMENT
        }
        if (inputAnnotations.size != 1) {
            return emptyList()
        }

        return try {
            val inputModelClassList =
                inputAnnotations[0].value as List<KSType> //due to vararg translating to list of elements
            inputModelClassList.map { it.declaration }.filterIsInstance<KSClassDeclaration>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    companion object {
        val visited = mutableListOf<String>()
        const val VISITIOR_NAME = "TestDeclarationVisitor"
        const val CTA_CONTAINER_ARGUMENT = "testContainer"
        const val CTA_INPUTMODEL_ARGUMENT = "classToTest"
    }
}
