package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.transpilerDataMap
/**
 * Function for preparing of inflows and outflows of stock
 *     prepareInflowsOfStock
 *     prepareOutflowsOfStock
 */
fun prepareInflowsOfStock(inflowsList: List<String>):String{
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
fun prepareOutflowsOfStock(outflowList: List<String>):String{
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
 * Function for preparing type of integration function in sim specs tag
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
 * Function for preparing equation with macro functions
 *     preparingEquationForMacroFunctions
 */

fun prepareEquationForBuiltInFunctions(valueEquation: String):String{
    if (checkContainsMacroFunction(valueEquation)) {
        return "/*$valueEquation*/"
    }
    return valueEquation
}

fun checkFirstChar(name: String): String {
    val validChars = listOf<Char>(
        'q',
        'w',
        'e',
        'r',
        't',
        'z',
        'u',
        'i',
        'o',
        'p',
        'a',
        's',
        'd',
        'f',
        'g',
        'h',
        'j',
        'k',
        'l',
        'y',
        'x',
        'c',
        'v',
        'b',
        'n',
        'm',
        'Q',
        'W',
        'E',
        'R',
        'T',
        'Z',
        'U',
        'I',
        'O',
        'P',
        'A',
        'S',
        'D',
        'F',
        'G',
        'H',
        'J',
        'K',
        'L',
        'Y',
        'X',
        'C',
        'V',
        'B',
        'N',
        'M'
    )
    var name1 = name
    for (index in name.indices) {
        if (validChars.contains(name[index])) {
            return name1
        } else {
            error("Invalid identifier: $name")
            // name1 = name.removeRange(index,index+1)
        }
    }
    return name1
}

fun checkLastChar(name: String): String {
    return name.dropLastWhile { it == '_' }
}

fun removeExtraSpace(name: String): String {
    var name1 = name
    for (index in name.indices) {
        if (name[index] == '_' && name[index + 1] == '_') {
            name1 = name1.removeRange(index, index + 1)
        }
    }
    return name1
}

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
 * Function for preparing equation values of stock or constants and transforming them in proper format for ksd toolkit
 * Function: preparingEquation
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

/**
 * Function for preparing equation for constants uppercase syntax
 *     preparingEquationForMacroFunctions
 */

fun prepareEquationsWithConstantsToUpperCase(valueEquation: String): String{
    var constantsList = mutableListOf<String>()
    var valueEquation=valueEquation
    constantsList = prepareEquation(constantsList, "EquationTokenValueConstant")
    for (constant in constantsList) {
        valueEquation = valueEquation.replace(constant, constant.uppercase())
    }
    return valueEquation
}

/**
 * Function for preparing equation to be CONSTANT or converter
 * Function: preparingEquation
 */

fun prepareEquationsInConstantsOrConverters(modelName:String,tagName: String,tagType: String, equation: String){
    var value = equation
    transpilerDataMap += try{value.toDouble()
        mapOf("$modelName $tagType"+"EquationTokenValueConstant: $tagName" to value)
    }catch (e: Exception){
        value = prepareEquationForBuiltInFunctions(value)
       // value = prepareEquationsWithConstantsToUpperCase(value)
        mapOf("$modelName $tagType"+"EquationTokenValueConverter: $tagName" to value)
    }

}

