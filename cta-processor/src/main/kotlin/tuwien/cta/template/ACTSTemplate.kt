package tuwien.cta.template

import tuwien.cta.input_model.CTAInputModel

/**
 * helper function to create an ACTS config file string by using a template and the input model.
 *
 * @param inputModel input model used in the config
 * @return String which contains the whole content of an ACTS config file.
 */
fun createACTSTemplate(inputModel: CTAInputModel): String {
    val parameters = inputModel.getParametersString()
    val constraints = inputModel.getConstraintsString()
    val templateBuilder = StringBuilder()
    templateBuilder.append("[System]\n")
    templateBuilder.append("Name: ${inputModel.fileName.getTestFileName()}\n")
    templateBuilder.append("\n")
    templateBuilder.append("[Parameter]\n")
    templateBuilder.append("$parameters\n")
    templateBuilder.append("[Constraint]\n")
    templateBuilder.append(constraints)
    return templateBuilder.toString()
}
