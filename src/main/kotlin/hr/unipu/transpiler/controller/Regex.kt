package hr.unipu.transpiler.controller

import java.util.regex.Pattern


const val NUMERIC_CONSTANTS_REGEX = "(?:\\b\\d+\\.?\\d*|\\b\\d*\\.?\\d*)(?:[eE][+-]?\\d+)?|(?:\\.\\d*|\\b\\d*\\.?\\d*)(?:[eE][+-]?\\d+)?"

fun getConstantsPosition(s: String): List<Pair<Int, Int>> {
    val pattern = Pattern.compile(NUMERIC_CONSTANTS_REGEX)
    val matcher = pattern.matcher(s)
    val constantsPosition = mutableListOf<Pair<Int, Int>>()
    while (matcher.find()) {
        if(matcher.group().isEmpty()) {
            continue
        }
        constantsPosition.add(Pair(matcher.start(), matcher.end()))
    }
    return constantsPosition
}

fun getConstantsValue(s: String,location: Pair<Int,Int>): String {
    return s.substring(location.first,location.second)
}

/*fun main() {
    val s = "(1- vacation)*(normal_workload394 + added_assignments*1.1 + 1e10)"
    getConstantsPosition(s).forEach({p -> println(s.substring(p.first, p.second)) })
    println("kraj")
}*/
