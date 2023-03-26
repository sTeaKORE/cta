package tuwien.cta.util

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.template.createACTSTemplate
import tuwien.cta.template.createTestTemplate
import tuwien.cta.testset_generation.CTATestset
import java.io.File
import java.io.FileNotFoundException

/**
 * helper class which is used to generate different files with the code generator
 *
 * @property codeGenerator code generator from ksp
 * @constructor create empty file helper
 */
class FileUtil(private val codeGenerator: CodeGenerator) {

    /**
     * generates the ACTS config file
     *
     * @param inputModel input model used for the ACTS config
     * @return generated config file
     * @throws FileNotFoundException if something went wrong during generation
     */
    fun generateACTSFile(inputModel: CTAInputModel): String {
        val filename = inputModel.fileName.getConfigFileNameNoExtension()
        val file = codeGenerator.createNewFile(Dependencies(false), "", filename, "txt")

        val actsTemplate = createACTSTemplate(inputModel)
        file.appendText(actsTemplate)
        file.close()

        val configFile = codeGenerator.generatedFile.find { it.name == inputModel.fileName.getConfigFileNameWithExtension() }
            ?: throw FileNotFoundException("Generated Config File not found")
        return configFile.absolutePath
    }

    /**
     * generates the combinatorial testing library file, or returns the existing one if present
     *
     * @return combinatorial testing library file
     * @throws FileNotFoundException if something went wrong during generation
     */
    fun getLibraryFile(): File {
        val filename = getLibraryName()
        val existingLibraryFile = codeGenerator.generatedFile.find { it.name == filename }
        if (existingLibraryFile != null) {
            return existingLibraryFile
        }

        codeGenerator.createNewFile(Dependencies(false), "", filename, "").close()

        return codeGenerator.generatedFile.find { it.name == filename }
            ?: throw FileNotFoundException("Generated Config File not found")
    }

    /**
     * generates the junit test file
     *
     * @param testSet test set used for junit test
     * @param testName class name of the test
     * @param containerClass container class from annotation
     * @param classesToTest classes to test from annotation
     */
    fun generateTestFile(testSet: CTATestset, testName: CTAFileName, containerClass: CTAFileName, classesToTest: List<KSClassDeclaration>) {
        val testFileCount = testSet.getTestfilesNeeded()
        for (i in 1..testFileCount) {
            val fileName = testName.getTestFileNamePart(i)
            val packageName = testName.getPackage()
            val testFile = codeGenerator.createNewFile(Dependencies(false), packageName, fileName)
            val testTemplate = createTestTemplate(testSet, testName, containerClass, classesToTest, i)
            testFile.appendText("$testTemplate\n")
            testFile.close()
        }
    }
}
