package tuwien.cta.visitors

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import tuwien.cta.testset_generation.CTAGeneratorConnector
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.testset_generation.CTATestset
import tuwien.cta.util.*
import java.lang.Exception

/**
 * Entry Point Visitor which orchestrates the whole process. Current process revolves around following steps:
 * 1) Search for testing annotation (CTATest) and parse all its parameters.
 * 2) Use the constraint parsing annotation visitor to generate an input model.
 * 3) Use the library to generate a test set.
 * 4) Use all above gathered information to generate the automated test.
 *
 * @property codeGenerator code generator from ksp
 * @property resolver resolver from ksp
 * @property loggingUtil logging util
 * @constructor Create empty Test declaration visitor
 */
class TestDeclarationVisitor(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val loggingUtil: LoggingUtil
) : KSVisitorVoid() {

    private val knownAnnotations = listOf("CTATest")
    private val generatorConnector = CTAGeneratorConnector()
    private val fileUtil = FileUtil(codeGenerator)

    /**
     * visitor function for functions, searches for test annotations on function and passes them to the annotation
     * visitor function
     *
     * @param function function from ksp
     * @param data optional data which can be passed between visitors
     */
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

    /**
     * visitor function for annotation
     * parses the test annotation and then passes the referenced class to the visitor which parses all constraint annotations,
     * afterwards uses the input model for test set generation and ultimately starts the file generation,
     * most of the above process is done in other places ,but they are all triggered from this method
     *
     * @param annotation annotation from ksp
     * @param data optional data which can be passed between visitors
     */
    override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Processing Annotation")

        val containerClass = extractContainerClass(annotation) ?: return
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Successfully extracted Container Class $containerClass")


        val inputModelClasses = extractInputModelClasses(annotation)
        if (inputModelClasses.isEmpty()) {
            loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> No Classes found for Input Model")
            return
        }

        // use one of the specified classes for testing for name and package
        val testName = extractClassNameAndPackage(inputModelClasses[0])
        var inputModel = CTAInputModel(testName)
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

        val testSetEntries = csvReader().readAll(testSetFile)
        val testSet = CTATestset(inputModel.getParameters(), testSetEntries)
        loggingUtil.log("$VISITIOR_NAME.visitAnnotation(${annotation.shortName.asString()}) >>> Generated Testset:\n$testSet")
        fileUtil.generateTestFile(testSet, testName, containerClass, inputModelClasses)
    }

    /**
     * helper function parse container class from annotation parameter
     *
     * @param annotation annotation from ksp
     * @return container class or null if not found
     */
    private fun extractContainerClass(annotation: KSAnnotation): CTAFileName? {
        val container = annotation.arguments.find {
            val argumentName = it.name
            argumentName != null && argumentName.asString() == CTA_CONTAINER_ARGUMENT
        } ?: return null
        val containerType = container.value as KSType
        val containerDeclaration = containerType.declaration
        return if (containerDeclaration is KSClassDeclaration) {
            extractClassNameAndPackage(containerDeclaration)
        } else {
            null
        }
    }

    /**
     * helper function to parse input model classes from annotation parameters
     *
     * @param annotation annotation from ksp
     * @return list of ksp class declarations which should be used as input model, returns an empty list if an error
     * occurred during parsing
     */
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

    /**
     * helper function to extract class name and package of given class
     *
     * @param classDeclaration ksp class declaration
     * @return name and package wrapped in the CTAFileName class
     */
    private fun extractClassNameAndPackage(classDeclaration: KSClassDeclaration): CTAFileName {
        val className = classDeclaration.simpleName.asString()
        val packageName = classDeclaration.packageName.asString()
        return CTAFileName(className, packageName)
    }

    companion object {
        val visited = mutableListOf<String>()
        const val VISITIOR_NAME = "TestDeclarationVisitor"
        const val CTA_CONTAINER_ARGUMENT = "testContainer"
        const val CTA_INPUTMODEL_ARGUMENT = "classToTest"
    }
}
