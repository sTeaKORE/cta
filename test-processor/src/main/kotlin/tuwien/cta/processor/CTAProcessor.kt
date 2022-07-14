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
        val symbols = resolver.getSymbolsWithAnnotation("tuwien.cta.annotation.test.CTATest")
        val ret = symbols.filter { !it.validate() }.toList()

        if (symbols.count() > 0) {
            val logFileName = "LogFile$counter"
            val loggingUtil = LoggingUtil(codeGenerator.createNewFile(Dependencies(false), "", logFileName, "log"))

            symbols
                .filter { it is KSFunctionDeclaration && it.validate() }
                .forEach { it.accept(TestDeclarationVisitor(codeGenerator, resolver, loggingUtil), Unit) }

            loggingUtil.close()
            counter += 1
        }

        return ret
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
