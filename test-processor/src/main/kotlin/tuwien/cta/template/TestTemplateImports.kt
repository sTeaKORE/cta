package tuwien.cta.template

import com.google.devtools.ksp.symbol.KSClassDeclaration
import tuwien.cta.input_model.parameters.CTAAbstractParameter
import tuwien.cta.input_model.parameters.CTAEnumParameter

class TestTemplateImports() {
    private val imports = mutableListOf<String>()

    constructor(classes: List<KSClassDeclaration>, parameters: List<CTAAbstractParameter>): this() {
        for (clazz in classes) {
            imports.add("${clazz.packageName.asString()}.${clazz.simpleName.asString()}")
        }
        parameters.filterIsInstance<CTAEnumParameter>().forEach { imports.add(it.getImport()) }
    }

    fun getImports(): List<String> {
        return imports.map { "import $it" }
    }
}
