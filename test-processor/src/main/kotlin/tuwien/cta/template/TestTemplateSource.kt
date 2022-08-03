package tuwien.cta.template

class TestTemplateSource(
    val packageName: String,
    val testClassName: String,
    val classToTest: String,
    val testCasesList: List<String>
) {
    fun getImport(): String {
        return "$packageName.$classToTest"
    }
}
