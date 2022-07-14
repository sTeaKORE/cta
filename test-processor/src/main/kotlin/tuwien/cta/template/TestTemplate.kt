package tuwien.cta.template

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
