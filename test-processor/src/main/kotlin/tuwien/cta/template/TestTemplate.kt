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
    val import = "$packageName.$testClass"

    return parseTemplate(packageName, className, import)
}

private fun parseTemplate(packageName: String, className: String, import: String) =
    """
    package $packageName
    
    import org.junit.jupiter.api.Test
    import $import
    
    class $className {

        @Test
        fun testingClass() {
            println("success")
        }
    }     
    """.trimIndent()
