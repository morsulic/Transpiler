package hr.unipu.transpiler.controller

/**
 * Function printModelsData prints all models data by every specific model
 */

fun printModelsData(map: MutableMap<String, Any>){
    val modelNames = map.filterKeys { it.contains("model names") }
    var names= modelNames.values.toString().removeSurrounding("[[","]]").split(", ")
    //println(names)
    for(index in names.indices){
        println(map.filterKeys { it.contains(names[index])})
    }

}