package tuwien.cta.template

import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.testset_generation.CTATestset
import tuwien.cta.util.CTAFileName

/**
 * helper function to create a complete junit 5 test file string, processes all information, parses it and returns the
 * string
 *
 * @param testSet generated test set from the combinatorial testing library
 * @param testName name of the test class
 * @param containerClass container class of the test (from annotation)
 * @param classesToTest classes used as input for testing (from annotation)
 * @return junit 5 test file string
 */
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

/**
 * takes the source and uses it to fill in the template and returns the filled in template.
 *
 * @param templateSource source containing all relevant information for the template
 * @return parsed template string
 */
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
    stringBuilder.append("${double_spacer}val inputArray = arrayOf(${getInputs(templateSource.classesToTest, false)}) as Array<Any>\n")
    stringBuilder.append("${double_spacer}testContainer.oracle(testContainer.testMethod, inputArray)\n")
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

/**
 * helper function which returns all parameters of input class, used to fill in parameters in helper functions inside
 * the test file
 *
 * @param classes all class (parameters) as list
 * @param withType boolean to describe if we only need the name or also the type
 * @return string concatenating all parameters with or without type.
 */
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
