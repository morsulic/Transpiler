package hr.unipu.transpiler.controller

import org.w3c.dom.Element

fun checkModelName(element: Element, i: Int): String {
    var modelName = element.getAttribute("name")
    if (!element.hasAttribute("name") && i > 0) error("Model missing 'name' attribute")
    else if (element.hasAttribute("name") && i == 0) error("Root model MUST NOT have attribute 'name'.")
    else if (!element.hasAttribute("name") && i == 0) modelName = modelRootNameConst
    if (modelName == null) error("Model 'name' attribute is empty!")

    return modelName
}

fun checkAlphaOrUnderscore(c: Char): Boolean {
    return c.isLetter() || c == '_'
}


fun checkContainsMacroFunction(equation: String?): Boolean {

    val builtInList = listOf<String>("AND", "OR", "NOT", "IF", "THEN", "ELSE", "STD", "ABS", "ARCCOS", "ARCSIN",
        "ARCTAN", "COS", "EXP", "INF", "INT", "LN", "LOG10", "MAX", "MIN", "PI", "SIN", "SQRT", "TAN", "EXPRND",
        "LOGNORMAL", "NORMAL", "POISSON", "RANDOM", "DELAY", "DELAY1", "DELAY3", "DELAYN", "FORCST", "SMTH1", "SMTH3",
        "SMTHN", "TREND", "PULSE", "RAMP", "STEP", "TIME", "INIT", "PREVIOUS", "MOD")

    if (equation != null) {
        for (operator in builtInList) {
            var start = 0
            while (equation.substring(start).contains(operator)) {
                start = equation.indexOf(operator, start)
                val stop = start + operator.length
                if ((start <= 0 || !checkAlphaOrUnderscore(equation[start - 1]) && (stop >= equation.length ||
                            !checkAlphaOrUnderscore(equation[stop])))) { return true }
                start = stop
            }

        }
    }
    return false
}