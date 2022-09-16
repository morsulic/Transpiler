package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.transpilerDataMap

/**
 * Function for preparing of inflows and outflows of stock:
 *     prepareInflowsOfStock
 *     prepareOutflowsOfStock
 */
fun prepareInflowsOfStock(inflowsList: List<String>): String {
    var flows = ""
    for (i in inflowsList.indices) {
        var x = inflowsList[i]
        var constantsList = mutableListOf<String>()
        constantsList = preparingEquation(constantsList, "constants")
        if (constantsList.contains(x.lowercase())) {
            x = x.uppercase()
        }
        flows = if (i == 0) {
            x
        } else {
            "$flows + ($x"
        }
    }
    if (inflowsList.size > 0) {
        flows += ")".repeat(inflowsList.size - 1)
    }
    return flows
}

fun prepareOutflowsOfStock(outflowList: List<String>): String {
    var flows = ""
    for (i in outflowList.indices) {
        var x = outflowList[i]
        var constantsList = mutableListOf<String>()
        constantsList = preparingEquation(constantsList, "constants")

        if (constantsList.contains(x.lowercase())) {
            x = x.uppercase()
        }
        flows = if (i == 0) {
            " - ($x"
        } else {
            "$flows + ($x"
        }
    }
    flows += ")".repeat(outflowList.size)
    return flows
}

/**
 * Function for preparing type of integration function in sim specs tag:
 *      preparingTypeOfSimSpecsIntegration
 */

fun prepareTypeOfSimSpecsIntegration(method: String): String {
    if (method.lowercase().contains("euler")) {
        return "EulerIntegration()"
    } else if (method.lowercase().contains("runge") || method.lowercase().contains("kutta")) {
        return "RungeKuttaIntegration()"
    }
    return "EulerIntegration()\t//EulerIntegration is default integration"
}

/**
 * Function for preparing equation with macro functions:
 *     preparingEquationForMacroFunctions
 */

fun prepareEquationForBuiltInFunctions(valueEquation: String): String {
    if (checkContainsMacroFunction(valueEquation)) {
        return "/*$valueEquation*/"
    }
    return valueEquation
}

fun prepareEquationForImplementedBuiltInFunctions(valueEquation: String): String {
    var result = checkContainsBuiltInImplementedFunctions(valueEquation)
    var builtInMap = mapOf(
        "STARTTIME" to "INITIAL_TIME_VALUE", "STOPTIME" to "FINAL_TIME_VALUE",
        "DT" to "TIME_STEP_VALUE"
    )
    var value = valueEquation
    if (result != "") {
        for (operator in builtInMap) {
            if (operator.key == result)
                value = valueEquation.replace(result, operator.value)
        }
    }
    return value
}

/**
 * Functions for preparing names for transpiling:
 *    checkFirstChar -> called from checkTranspilerData.kt
 *    checkLastChar -> called from checkTranspilerData.kt
 *    removeExtraSpace -> called from removeTranspilerData.kt
 *    margeNameRules
 *    prepareNamesForTranspiling
 */

fun margeNameRules(name: String): String {
    var name1 = name
    name1 = checkFirstChar(name1)
    name1 = checkLastChar(name1)
    name1 = removeExtraSpace(name1)
    return name1
}

fun prepareNamesForTranspiling(tokens: MutableList<String>): MutableList<String> {
    var counter = 1
    var name = ""
    var nameHelp = ""

    var operatorsAndBulitInFunctions = listOf<String>(
        "AND",
        "OR",
        "NOT",
        "IF",
        "THEN",
        "ELSE",
        "STD",
        "ABS",
        "ARCCOS",
        "ARCSIN",
        "ARCTAN",
        "COS",
        "EXP",
        "INF",
        "INT",
        "LN",
        "LOG10",
        "MAX",
        "MIN",
        "PI",
        "SIN",
        "SQRT",
        "TAN",
        "EXPRND",
        "LOGNORMAL",
        "NORMAL",
        "POISSON",
        "RANDOM",
        "DELAY",
        "DELAY1",
        "DELAY3",
        "DELAYN",
        "FORCST",
        "SMTH1",
        "SMTH3",
        "SMTHN",
        "TREND",
        "PULSE",
        "RAMP",
        "STEP",
        "DT",
        "STARTTIME",
        "STOPTIME",
        "TIME",
        "INIT",
        "PREVIOUS",
        "MOD"
    )

    for (index in tokens.indices) {
        if (tokens[index].contains("name=")) {
            tokens[index] = tokens[index].replace("\\n", "_")
            tokens[index] = tokens[index].replace("\\\\", "")
            tokens[index] = tokens[index].replace("\\\"", "")
            for (i in tokens[index].indices) {
                if (tokens[index][i] == '"') {
                    counter++
                } else if (counter % 2 == 0 && tokens[index][i] == ' ') {
                    tokens[index] = tokens[index].substring(0, i) + '_' + tokens[index].substring(i + 1)
                } else if (tokens[index][i] == '"' && counter % 2 == 0) {
                    counter++
                }
            }
            nameHelp = getWantedString(tokens[index], "name")
            name = getWantedString(tokens[index], "name")
            name = margeNameRules(name)
            if (operatorsAndBulitInFunctions.contains(name.uppercase())) {
                error("You can not use this name: $name as your name because it is bulit-in global variable!!!")
            }
            tokens[index] = tokens[index].replace(nameHelp, name)
        }
    }

    return tokens
}

/**
 * Functions for preparing equation values of stock or constants and transforming them in proper format for ksd toolkit
 *     prepareEquation
 *     prepareEquationsWithConstantsToUpperCase -> for preparing equation for constants uppercase syntax
 *     prepareEquationsStockNames
 */

fun prepareEquation(list: MutableList<String>, nameKey: String): MutableList<String> {

    var list = list
    if (transpilerDataMap.contains("$nameKey")) {
        val str = transpilerDataMap.getValue("$nameKey")
        if (str is String) {
            list = str.split(",").toMutableList()
        }
    } else {
        list = mutableListOf<String>()
    }
    return list
}

fun prepareEquationsWithConstantsToUpperCase(valueEquation: String): String {
    var constantsList = mutableListOf<String>()
    var valueEquation = valueEquation
    constantsList = prepareEquation(constantsList, "constants")
    for (constant in constantsList) {
        valueEquation = valueEquation.replace(constant, constant.uppercase())
    }
    return valueEquation
}

fun prepareEquationsStockNames(valueEquation: String): String {
    var stockList = mutableListOf<String>()
    var valueEquation = valueEquation
    stockList = prepareEquation(stockList, "stocks")
    for (constant in stockList) {
        valueEquation = valueEquation.replace(constant, constant.lowercase().replaceFirstChar { it.uppercase() })
    }
    return valueEquation
}

/**
 * Function for preparing equation to be CONSTANT or converter
 * Function: preparingEquation
 */

fun prepareEquationsInConstantsOrConverters(modelName: String, tagName: String, tagType: String, equation: String) {
    var value = equation
    try {
        value.toDouble()
        transpilerDataMap += mapOf("$modelName $tagType" + "EquationTokenValueConstant: $tagName" to value)
        transpilerDataMap += mapOf("constants" to transpilerDataMap.getValue("constants") + "," + tagName)
    } catch (e: Exception) {
        println(e)
        value = prepareEquationForBuiltInFunctions(value)
        value = prepareEquationForImplementedBuiltInFunctions(value)
        transpilerDataMap += mapOf("$modelName $tagType" + "EquationTokenValueConverter: $tagName" to value)
    }

}



