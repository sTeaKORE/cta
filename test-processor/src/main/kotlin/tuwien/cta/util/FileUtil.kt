package tuwien.cta.util

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.input_model.CTAInputModel
import tuwien.cta.template.createACTSemplate
import tuwien.cta.template.createTestTemplate
import java.io.File
import java.io.FileNotFoundException

class FileUtil(private val codeGenerator: CodeGenerator) {

    fun generateACTSFile(inputModel: CTAInputModel): String {
        val filename = inputModel.getFilenameNoExtension()
        val file = codeGenerator.createNewFile(Dependencies(false), "", filename, "txt")

        val actsTemplate = createACTSemplate(inputModel)
        file.appendText(actsTemplate)
        file.close()

        val configFile = codeGenerator.generatedFile.find { it.name == inputModel.getFilenameWithExtension() }
            ?: throw FileNotFoundException("Generated Config File not found")
        return configFile.absolutePath
    }

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

    fun generateTestFile(inputModel: CTAInputModel, classToTest: KSClassDeclaration, inputContainer: String) {
        val fileName = inputModel.getTestFilename()
        val testFile = codeGenerator.createNewFile(Dependencies(false), classToTest.packageName.asString(), fileName)
        val testTemplate = createTestTemplate(inputModel, classToTest, fileName, inputContainer)
        testFile.appendText("$testTemplate\n")
        testFile.close()
    }
}
