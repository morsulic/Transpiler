package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.transpilerDataMap
import hr.unipu.transpiler.globalVariables.transpilerDataMapKsdToolkitSet

fun setDataForTranspiling() {
    val modelNameList = transpilerDataMap.getValue("ModelNamesListInStringForm").removeSurrounding("[", "]").split(", ")
    for (modelName in modelNameList) {
        setTagsOfWantedData(modelName, "Stock", "StockName:")
        setTagsOfWantedData(modelName, "Flow", "FlowName:")
        setTagsOfWantedData(modelName, "Aux", "AuxName:")
        setTagsOfWantedData(modelName, "Module", "ModuleName:")
    }
}

fun setTagsOfWantedData(modelName: String, tagType: String, tagMapName: String) {
    val tagNameList = transpilerDataMap.filterKeys { it.contains("$modelName $tagMapName") }.values
    for (tagName in tagNameList) {
        setWantedData(modelName, tagType, tagName)
    }

}

fun setWantedData(modelName: String, tagType: String, tagName: String) {
    val helpMap = transpilerDataMap.filterKeys { it.contains(modelName) && it.contains(tagType) && it.contains(tagName) }
    val name = helpMap.getValue("$modelName $tagType" + "Name: $tagName")

    var equationValueConstant: String? = null
    var equationValueConverter: String? = null
    var nonNegativeValue: String? = null
    var unitValue: String? = null
    var descriptionValue: String? = null
    var stockInflowOutflowValue: String? = null
    var connectionTo: String? = null
    var connectionFrom: String? = null

    if (tagType != "Module") {
        try {
            equationValueConstant = helpMap.getValue("$modelName $tagType" + "EquationTokenValueConstant: $tagName")
        } catch (e: Exception) {
        }
        try {
            equationValueConverter = helpMap.getValue("$modelName $tagType" + "EquationTokenValueConverter: $tagName")
        } catch (e: Exception) {
        }
        //nonNegativeValue = helpMap.getValue("$modelName $tagType" + "NonNegativeValueOf: $tagName")
        try {
            unitValue = helpMap.getValue("$modelName $tagType" + "UnitValue: $tagName")
        } catch (e: Exception) {
        }
    }
    try {
        descriptionValue = helpMap.getValue("$modelName $tagType" + "DescriptionValue: $tagName")
    } catch (e: Exception) {
    }
    if (tagType == "Stock") {
        stockInflowOutflowValue = helpMap.getValue("$modelName $tagType" + "InflowOutflowValue: $tagName")
    }
    if (tagType == "Module") {
        connectionTo = helpMap.getValue("$modelName $tagType" + "ConnectionsTo: $tagName")
        connectionFrom = helpMap.getValue("$modelName $tagType" + "ConnectionsFrom: $tagName")
    }

    setCompanionObjects(name, modelName, equationValueConstant)
    setSystemElements(name, modelName, tagType, equationValueConstant, equationValueConverter)
    setUnitsAndDescription(name, modelName, unitValue, descriptionValue)
    if (tagType == "Stock") setInitialValueOfStock(name, modelName, equationValueConstant, equationValueConverter)
    setVariablesEquation(
        name,
        modelName,
        tagType,
        equationValueConstant,
        equationValueConverter,
        stockInflowOutflowValue
    )
    setBehaviorNonNegativeBaseLevel(name, modelName, nonNegativeValue)

    /* printSetData(
         name, equationValueConstant, equationValueConverter, stockInflowOutflowValue, nonNegativeValue,
         unitValue, descriptionValue, connectionTo, connectionFrom, modelName, tagType
     )*/
}


fun setCompanionObjects(name: String, modelName: String, equationValueConstant: String?) {
    if (equationValueConstant != null) {
        transpilerDataMapKsdToolkitSet +=
            mapOf(
                "$modelName $name companion object keys" to
                        "const val ${name.uppercase()}" + "_KEY = \"${name.uppercase()}\""
            )
        transpilerDataMapKsdToolkitSet +=
            mapOf(
                "$modelName $name companion object values" to
                        "const val ${name.uppercase()}" + "_VALUE = $equationValueConstant"
            )
    }
}

fun setSystemElements(
    name: String, modelName: String, tagType: String, equationValueConstant: String?,
    equationValueConverter: String?
) {
    if (equationValueConstant != null && tagType == "Aux") transpilerDataMapKsdToolkitSet +=
        mapOf(
            "$modelName $name 2. Constants" to
                    "val ${name.uppercase()} = model.constant(${name.uppercase()}" + "_KEY)"
        )
    if (tagType == "Stock")
        transpilerDataMapKsdToolkitSet += mapOf("$modelName $name 2. Stocks"
                to "val ${name.lowercase().replaceFirstChar { it.uppercase()}} = model.stock(\"$name\")")
    if (tagType == "Flow") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 2. Flows" to "val ${name.lowercase()} = model.flow(\"$name\")")
    if (equationValueConverter != null && tagType == "Aux") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 2. Converters" to "val ${name.lowercase()} = model.converter(\"$name\")")

}

fun setUnitsAndDescription(name: String, modelName: String, unitValue: String?, descriptionValue: String?) {
    if (unitValue != null) transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 2. Entities unit" to "$name.unit =  \"$unitValue\" ")
    if (descriptionValue != null) transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 2. Entities description" to "$name.description = \"$descriptionValue\"")
}

fun setInitialValueOfStock(
    name: String,
    modelName: String,
    equationValueConstant: String?,
    equationValueConverter: String?
) {
    if (equationValueConstant != null) transpilerDataMapKsdToolkitSet += mapOf("$modelName $name 3. Stocks"
                to "${name.lowercase().replaceFirstChar { it.uppercase()}}.initialValue = {$equationValueConstant}")
    if (equationValueConverter != null) transpilerDataMapKsdToolkitSet += mapOf("$modelName $name 3. Stocks"
            to "${name.lowercase().replaceFirstChar { it.uppercase()}}.initialValue = {$equationValueConverter}")
}

fun setVariablesEquation(
    name: String, modelName: String, tagType: String, equationValueConstant: String?,
    equationValueConverter: String?, stockInflowOutflowValue: String?
) {

    if (equationValueConstant != null && tagType == "Aux") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 4. Constants" to "${name.uppercase()}.equation = {${name.uppercase()}_VALUE}")
    if (stockInflowOutflowValue != null)
        transpilerDataMapKsdToolkitSet += mapOf("$modelName $name 4. Stocks"
                to "${name.lowercase().replaceFirstChar { it.uppercase()}}.equation = {$stockInflowOutflowValue}")

    if (equationValueConverter != null && tagType == "Flow") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 4. Flows" to "${name.lowercase()}.equation = {$equationValueConverter}")
    if (equationValueConverter != null && tagType == "Aux") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name 4. Converters" to "${name.lowercase()}.equation = {$equationValueConverter}")
}

fun setBehaviorNonNegativeBaseLevel(name: String, modelName: String, nonNegativeValue: String?) {
    if (nonNegativeValue != null && nonNegativeValue != "false") transpilerDataMapKsdToolkitSet +=
        mapOf("$modelName $name Behavior" to "Non negative value = $nonNegativeValue")
}