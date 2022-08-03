package tuwien.cta.template

import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.input_model.CTAInputModel

fun createTestTemplate(packageName: String, className: String): String =
    """
    package $packageName
    
    import org.junit.jupiter.api.Test
    
    class $className {

        @Test
        fun testingClass() {
            println("success")
        }
    }     
    """.trimIndent()

fun createTestTemplate(inputModel: CTAInputModel, classToTest: KSClassDeclaration, className: String): String {
    val packageName = classToTest.packageName.asString()
    val testClass = classToTest.simpleName.asString()
    val testSetList = inputModel.getTestSetArguments()

    val templateSource = TestTemplateSource(
        packageName,
        className,
        testClass,
        testSetList
    )

    return parseTemplate(templateSource)
}

private fun parseTemplate(templateSource: TestTemplateSource) =
    """
    package ${templateSource.packageName}
    
    import org.junit.jupiter.params.ParameterizedTest
    import org.junit.jupiter.params.provider.Arguments
    import org.junit.jupiter.params.provider.MethodSource
    import java.util.stream.Stream
    import ${templateSource.getImport()}
    
    class ${templateSource.testClassName} {

        @ParameterizedTest
        @MethodSource("testArguments")
        fun testingClass(input: ${templateSource.classToTest}) {
            println("success")
        }
        
        companion object {
            @JvmStatic
            fun testArguments() = Stream.of(
                ${templateSource.testCasesList.joinToString(separator = ",\n"){"Arguments.of(create${templateSource.classToTest}($it))"}}
            )
            
            @JvmStatic
            private fun create${templateSource.classToTest}(): ${templateSource.classToTest} {
                return ${templateSource.classToTest}()
            }
        }
        

    }     
    """.trimIndent()
