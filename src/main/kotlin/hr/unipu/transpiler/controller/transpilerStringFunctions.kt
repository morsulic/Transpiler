package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.*
import hr.unipu.transpiler.globalVariables.transpilerDataMap
import hr.unipu.transpiler.globalVariables.transpilerDataMapKsdToolkitSet
import hr.unipu.transpiler.globalVariables.transpilerString

val tab = "\t".repeat(4)

fun transpilerStringPackageAndImports() {
    transpilerString += packageAndImports
}

fun transpilerStringDescriptionText() {
    transpilerString += "\n" + "/**\n"
    transpilerString += " * XMILE version = " + transpilerDataMap.getValue("Version") + "\n"
    transpilerString += " * xmlns = " + transpilerDataMap.getValue("xmlns1") + "\n"
    transpilerString += " * xmlnsISEE = " + transpilerDataMap.getValue("xmlnsISEE") + "\n"
    transpilerString += " * Model name = " + transpilerDataMap.getValue("Model name").lowercase()
        .replaceFirstChar { it.uppercase() } + "\n"
    transpilerString += " * Vendor = " + transpilerDataMap.getValue("Vendor") + "\n"
    transpilerString += " * Product name = " + transpilerDataMap.getValue("Product name") + "\n"
    transpilerString += " * Product version = " + transpilerDataMap.getValue("Product version") + "\n*/\n\n"

}

fun transpilerStringCompanionObjects(modelName: String) {
    transpilerString += constantValuesBeginning
    transpilerString += "// companion object keys\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("companion object keys") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "// companion object values\n"
    transpilerString +=   transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("companion object values") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "// companion object simSpec\n"
    if (transpilerDataMap.contains("$modelName SimSpecs empty:")) {
        transpilerString +=  "const val INITIAL_TIME_VALUE = " + transpilerDataMap.getValue("default SimSpecs Initial time:") + "\n"
        transpilerString +=  "const val FINAL_TIME_VALUE = " + transpilerDataMap.getValue("default SimSpecs Final time:") + "\n"
        transpilerString +=  "const val TIME_STEP_VALUE = " + transpilerDataMap.getValue("default SimSpecs Time step:") + "\n\n}"
    } else {
        transpilerString +=  "const val INITIAL_TIME_VALUE = " + transpilerDataMap.getValue("$modelName SimSpecs Initial time:") + "\n"
        transpilerString +=  "const val FINAL_TIME_VALUE = " + transpilerDataMap.getValue("$modelName SimSpecs Final time:") + "\n"
        transpilerString +=  "const val TIME_STEP_VALUE = " + transpilerDataMap.getValue("$modelName SimSpecs Time step:") + "\n\n}"
    }
    transpilerString += initializationOfModelCreateAllSystemElements
    if (transpilerDataMap.contains("$modelName SimSpecs empty:")) {
        transpilerString += "model.integrationType = " + transpilerDataMap.getValue("default SimSpecs Method:") + "\n"
        transpilerString += "model.name = \"$modelName\" // name is optional\n"
        transpilerString += "model.timeUnit = \"" + transpilerDataMap.getValue("default SimSpecs Time unit:") +
                "\" // time unit is optional\n"

    } else {
        transpilerString +=  "model.integrationType = " + transpilerDataMap.getValue("$modelName SimSpecs Method:") + "\n"
        transpilerString +=  "model.name = \"$modelName\" // name is optional"
        transpilerString +=  "model.timeUnit = \"" + transpilerDataMap.getValue("$modelName SimSpecs Time unit:") +
                "\" // time unit is optional\n"
    }
}

fun transpilerStringCreateAllSystemElements(modelName: String) {
    transpilerString += createAllSystemElements
    transpilerString += "//2. Constants\n"
    transpilerString += transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("2. Constants") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//2. Stocks\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("2. Stocks") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//2. Flows\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("2. Flows") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//2. Converters\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("2. Converters") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
}

fun transpilerStringCreateInitialValues(modelName: String) {
    transpilerString += initialValues
    transpilerString += "// 3. Stocks\n"
    transpilerString += transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("3. Stocks") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
}

fun transpilerStringEquations(modelName: String) {
    transpilerString += equations
    transpilerString += "//4. Constants\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("4. Constants") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//4. Stocks\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("4. Stocks") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//4. Flows\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("4. Flows") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
    transpilerString += "//4. Converters\n"
    transpilerString +=  transpilerDataMapKsdToolkitSet.filterKeys { it.contains("$modelName") && it.contains("4. Converters") }.values.toString()
        .removeSurrounding("[", "]").replace(',', '\n') + "\n"
}

fun transpilerStringCreator() {
    val modelNameList = transpilerDataMap.getValue("ModelNamesListInStringForm").removeSurrounding("[", "]").split(", ")
    for (modelName in modelNameList) {
        transpilerStringPackageAndImports()
        transpilerStringDescriptionText()
        transpilerStringCompanionObjects(modelName)
        transpilerStringCreateAllSystemElements(modelName)
        transpilerStringCreateInitialValues(modelName)
        transpilerStringEquations(modelName)
    }
}

