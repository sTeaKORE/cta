package tuwien.cta.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import tuwien.cta.util.LoggingUtil
import tuwien.cta.util.appendText
import tuwien.cta.visitors.ConstraintAnnotationVisitor
import tuwien.cta.visitors.TestDeclarationVisitor
import java.io.OutputStream

/**
 * Current plan for process:
 * 1) collect constraints
 * 2) call external program to convert constraints to testcases
 * 3) translate test cases to usable test cases
 * 4) hook up testcases with generated automatic test
 * 5) done
 */
class CTAProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        //check if debug mode is enabled
        val isDebug = resolver.getSymbolsWithAnnotation("tuwien.cta.annotation.utility.CTADebug").count() > 0

        //get all CTATest annotations
        val symbols = resolver.getSymbolsWithAnnotation("tuwien.cta.annotation.test.CTATest")

        //process all valid CTATest annotations
        if (symbols.count() > 0) {
            val loggingUtil = createLoggingUtil(isDebug)

            symbols
                .filter { it is KSFunctionDeclaration && it.validate() }
                .forEach { it.accept(TestDeclarationVisitor(codeGenerator, resolver, loggingUtil), Unit) }

            loggingUtil.close()
        }

        //return all invalid symbols for possible future processing
        return symbols.filter { !it.validate() }.toList()
    }

    private fun createLoggingUtil(isDebug: Boolean): LoggingUtil {
        val logFileName = "LogFile$counter"
        counter += 1
        return if (isDebug) {
            LoggingUtil(
                codeGenerator.createNewFile(
                    Dependencies(false),
                    "",
                    logFileName,
                    "log"
                )
            )
        } else {
            LoggingUtil(null)
        }
    }

    companion object {
        var counter = 0
    }
}

class CTAProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return CTAProcessor(environment.codeGenerator)
    }
}
