package tuwien.cta.template

import tuwien.cta.input_model.CTAInputModel

fun createACTSemplate(inputModel: CTAInputModel): String {
    val parameters = inputModel.getParametersString()
    val templateBuilder = StringBuilder()
    templateBuilder.append("[System]\n")
    templateBuilder.append("Name: ${inputModel.fileName.getTestFileName()}\n")
    templateBuilder.append("\n")
    templateBuilder.append("[Parameter]\n")
    templateBuilder.append("$parameters\n")
    templateBuilder.append("[Constraint]\n")
    return templateBuilder.toString()
}
