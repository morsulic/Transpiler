package hr.unipu.transpiler.controller

import hr.unipu.transpiler.ModelTemplate.*
/**
 * Functions creating string that is transpiled in ksdtoolkit model
 */
fun creatingTransStringHelpFunction(i: Int,index: Int, entry: Any,token: String, tokens: String,
                                    transString: MutableList<String>,counter: MutableList<Int>){
    if (tokens == token && counter[i] == 0 ){
        counter[i]++
        transString[index] += "\n\t\t\t\t// $token\n"+"\t\t\t\t\t\t${entry} \n"
    }else if (tokens == token){
        transString[index] += "\t\t\t\t\t\t${entry} \n"
    }

}

fun transpiledModelsData(map: MutableMap<String, Any>){
    val modelNames = map.filterKeys { it.contains("model names") }
    var names = modelNames.values.toString().removeSurrounding("[[","]]").split(", ")
    //println(names)
    var tokens = listOf<String>("companion object keys","companion object values","2. Variables constants",
        "2. Variables converters","2. Stocks","2. Flows","2. Entities unit","2. Entities description",
        "3. Stocks", "4. Variables constant","4. Stocks","4. Flows", "4. Converters")
    var counter = mutableListOf<Int>(0,0,0,0,0,0,0,0,0,0,0,0,0)
    var transString = mutableListOf<String>()

    for(index in names.indices){
        transString.add(index,descriptionPackage + constantValuesBeginning)
        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in map.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(i,index,entry.value,"companion object keys",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"companion object values",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Variables constants",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Variables converters",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Stocks",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Flows",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Entities unit",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"2. Entities description",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"3. Stocks",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"4. Variables constants",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"4. Stocks",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"4. Flows",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(i,index,entry.value,"4. Converters",tokens[i],transString,counter)

                   //println("${entry.key} : ${entry.value}")
                }
            }
        }
        transString[index]+="\n" +
                "\t\t\t}\n}"
    }
    println(transString)
}