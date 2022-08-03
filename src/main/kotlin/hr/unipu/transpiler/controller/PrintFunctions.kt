package hr.unipu.transpiler.controller

/**
 * Function printModelsData prints all models data by every specific model
 */

fun printModelsData(map: MutableMap<String, Any>){
    val modelNames = map.filterKeys { it.contains("model names") }
    var names = modelNames.values.toString().removeSurrounding("[[","]]").split(", ")
    //println(names)
    var tokens = listOf<String>("companion object","2. Variables","2. Variables->converter","2.Stocks","2. Flows",
        "3. Stocks", "4. Variables","4. Stocks","4. Flows","4. Converters")

    for(index in names.indices){
        for(i in tokens.indices){
            for (entry in map.entries.iterator()) {
                if(entry.key.contains(names[index])&& entry.key.contains(tokens[i])){
                   println("${entry.key} : ${entry.value}")}
            }
        }
    }
}