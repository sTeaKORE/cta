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
 * Combinatorial testing annotation processor. First entry point of the whole process. Searches for annotations and
 * starts relevant processes.
 *
 * @property codeGenerator Generator which is used to generate the automated test suite.
 * @constructor Create empty Combinatorial testing annotation processor
 */
class CTAProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    /**
     * Starts the processing of the code base. This function searches for the debug annotation and creates a log file if
     * present as well as searches for all CTATest annotations and starts first process of reading annotation by passing
     * each annotation to a visitor.
     *
     * @param resolver class from ksp which can be used to search for annotations
     * @return list of invalid annotations which could be used in further processors
     */
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

    /**
     * Creates a logging util, which is a wrapper around a logging file. If the debug annotation is present it generates
     * a logging file with the code generator.
     *
     * @param isDebug boolean representing if debug annotation was present
     * @return new LoggingUtil
     */
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

/**
 * Provider Class which creates our processor. This class is referenced by the ksp library so ksp knows which processors
 * exist and have to be executed.
 *
 * @constructor Create empty provider.
 */
class CTAProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return CTAProcessor(environment.codeGenerator)
    }
}
