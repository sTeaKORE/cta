package tuwien.cta.template

import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.testset_generation.CTATestset
import tuwien.cta.util.CTAFileName

fun createTestTemplate(
    testSet: CTATestset,
    testName: CTAFileName,
    containerClass: String,
    classesToTest: List<KSClassDeclaration>
): String {
    val imports = TestTemplateImports(classesToTest, testSet.parameters)
    val source =
        TestTemplateSource(testSet, imports, testName, containerClass, classesToTest.map { it.simpleName.asString() })

    return parseTemplate(source)
}

const val spacer = "    "
const val double_spacer = "$spacer$spacer"
const val triple_spacer = "$spacer$spacer$spacer"
private fun parseTemplate(templateSource: TestTemplateSource): String {
    val stringBuilder = StringBuilder()

    stringBuilder.append("package ${templateSource.className.getPackage()}\n")
    stringBuilder.append("\n")
    stringBuilder.append("import org.junit.jupiter.params.ParameterizedTest\n")
    stringBuilder.append("import org.junit.jupiter.params.provider.Arguments\n")
    stringBuilder.append("import org.junit.jupiter.params.provider.MethodSource\n")
    stringBuilder.append("import java.util.stream.Stream\n")
    stringBuilder.append("import tuwien.cta.demo.FunctionClass\n")

    for (import in templateSource.imports.getImports()) {
        stringBuilder.append("$import\n")
    }

    stringBuilder.append("\n")
    stringBuilder.append("class ${templateSource.className.getTestFileName()} {\n")
    stringBuilder.append("\n")
    stringBuilder.append("$spacer@ParameterizedTest\n")
    stringBuilder.append("$spacer@MethodSource(\"testArguments\")\n")
    stringBuilder.append("${spacer}fun testingClass(")
    stringBuilder.append(getInputs(templateSource.classesToTest, true))
    stringBuilder.append(") {\n")
    stringBuilder.append("${double_spacer}println(\"Executing automatically generated ct test\")\n")
    stringBuilder.append("${double_spacer}val testContainer = ${templateSource.containerClass}()\n")
    stringBuilder.append("${double_spacer}testContainer.testMethod.call(${getInputs(templateSource.classesToTest, false)})\n")
    stringBuilder.append("${double_spacer}testContainer.oracle(\"todo\")\n")
    stringBuilder.append("${spacer}}\n")
    stringBuilder.append("\n")

    stringBuilder.append("${spacer}companion object {\n")
    stringBuilder.append("${double_spacer}@JvmStatic\n")
    stringBuilder.append("${double_spacer}fun testArguments() = Stream.of(\n")

    //currently only supporting single test objects
    val classToTest = templateSource.classesToTest[0]
    templateSource.testSet.getTestSetArguments().forEach {
        stringBuilder.append("${triple_spacer}Arguments.of(create${classToTest}(${it})),\n")
    }

    stringBuilder.append("${double_spacer})\n")
    stringBuilder.append("\n")
    stringBuilder.append("${double_spacer}@JvmStatic\n")
    stringBuilder.append("${double_spacer}private fun create${classToTest}(${templateSource.getParameterString()}): $classToTest {\n")
    stringBuilder.append("${templateSource.getInitializingFunction(triple_spacer)}\n")
    stringBuilder.append("${double_spacer}}\n")
    stringBuilder.append("${spacer}}\n")
    stringBuilder.append("}")

    return stringBuilder.toString()
}

private fun getInputs(classes: List<String>, withType: Boolean): String {
    val stringBuilder = StringBuilder()
    classes.forEachIndexed { index, clazz ->
        if (withType) {
            stringBuilder.append("input$index : $clazz")
        } else {
            stringBuilder.append("input$index")
        }

        if (index + 1 != classes.size) {
            stringBuilder.append(", ")
        }
    }
    return stringBuilder.toString()
}
