package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.transpilerDataMap

/**
 * 1. Function for preparing of inflows and outflows of stock
 *     preparingInflowsAndOutflowsOfStock
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
fun preparingInflowsAndOutflowsOfStock(flowsList: MutableList<MutableList<String>>,flowType: String): String {
    var flows = ""
    if (flowType=="inflows"){
    for (i in flowsList.indices) {
        var x = flowsList[i][1]
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
      if (flowsList.size > 0) {
        flows += ")".repeat(flowsList.size - 1)
     }
    }else{
        for (i in flowsList.indices) {
            var x = flowsList[i][1]
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
        flows += ")".repeat(flowsList.size)
    }
    return flows
}

/**
 * 2. Function for preparing equation with macro functions
 *     preparingEquationForMacroFunctions
 */

fun preparingEquationForMacroFunctions(valueEquation: String):String{
    if (equationContainsMacroFunction(valueEquation)) {
        return "/*$valueEquation*/"
    }
    return valueEquation
}

/**
 * 3. Function for preparing equation for constants uppercase syntax
 *     preparingEquationForMacroFunctions
 */

fun preparingEquationsWithConstantsToUpperCase(valueEquation: String): String{
    var constantsList = mutableListOf<String>()
    var valueEquation=valueEquation
    constantsList = preparingEquation(constantsList, "constants")
    for (constant in constantsList) {
        valueEquation = valueEquation.replace(constant, constant.uppercase())
    }
    return valueEquation
}

/**
 * 4. Function for preparing tags that non_negative value set
 *
 */

fun preparingTagNonNegativeStatus(tokenList: MutableList<String>): Boolean {
    val nonNegativeOne = tokenList.contains("<non_negative/>")
    val nonNegativeTwo = tokenList.contains("</non_negative>")
    return nonNegativeOne || nonNegativeTwo
}

/**
 * 5. Function for preparing type of integration function in sim specs tag
 *      preparingTypeOfSimSpecsIntegration
 */

fun preparingTypeOfSimSpecsIntegration(method: String): String {
    if (method.lowercase().contains("euler")) {
        return "EulerIntegration()"
    } else if (method.lowercase().contains("runge") || method.lowercase().contains("kutta")) {
        return "RungeKuttaIntegration()"
    }
    return "EulerIntegration()\t//EulerIntegration is default integration"
}

/**
 * 6. Function for preparing equation values of stock or constants and transforming them in proper format for ksd toolkit
 * Function: preparingEquation
 */

fun preparingEquation(list: MutableList<String>, nameKey: String): MutableList<String> {

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
 * 7. Function for preparing equation to be CONSTANT or converter
 * Function: preparingEquation
 */

fun preparingEquationsInConstantsOrConverters(modelName:String,tagName: String,tagType: String, equation: String){
    var value = equation
    transpilerDataMap += try{value.toDouble()
        mapOf("$modelName $tagType"+"EquationTokenValueConstant: $tagName" to value)
    }catch (e: Exception){
        value = preparingEquationForMacroFunctions(value)
        value = preparingEquationsWithConstantsToUpperCase(value)
        mapOf("$modelName $tagType"+"EquationTokenValueConverter: $tagName" to value)
    }
}