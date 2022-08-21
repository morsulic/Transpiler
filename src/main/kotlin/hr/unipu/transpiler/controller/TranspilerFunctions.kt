package hr.unipu.transpiler.controller
import hr.unipu.transpiler.ModelTemplate.*
/**
 * Functions creating string that is transpiled in ksdtoolkit model
 */
//Helping print function for simSpec data
fun printSimSpecDataInModels(map: MutableMap<String,Any>,names: List<String>){
    for(index in names.indices) {
        var name = names[index]
        println(map.filterKeys { it.contains("Model name $name") })
        println(map.filterKeys { it.contains("Method $name") })
        println(map.filterKeys { it.contains("Time unit $name") })
        println(map.filterKeys { it.contains("Initial time $name") })
        println(map.filterKeys { it.contains("Final time $name") })
        println(map.filterKeys { it.contains("Time step $name") })
        println("\n \n")
    }
}

//Helping print function for modules data
fun printModulesDataInModels(map: MutableMap<String,Any>,names: List<String>){
    for(index in names.indices){
        var name =names[index]
        println(""+(map.filterKeys { it.contains("2. Modules $name") }) +"\n")
        println(""+(map.filterKeys { it.contains("Module name $name") })+"\n")
        println(""+(map.filterKeys { it.contains("2. Entities description $name") }) +"\n")
        println(""+(map.filterKeys { it.contains("connection to $name") }) +"\n")
        println(""+(map.filterKeys { it.contains("connection from $name") }) +"\n")
    }
}

//Helping print function for header data
fun printHeaderDataInModels(map: MutableMap<String,Any>,names: List<String>){
    for(index in names.indices) {
        var name=names[index]
        println(name)
        println("" + (map.filterKeys { it.contains("Vendor") }))
        println("" + (map.filterKeys { it.contains("Product name") }))
        println("" + (map.filterKeys { it.contains("Product version") }))
        println("" + (map.filterKeys { it.contains("Options SubModel") }) + "\n")
    }

}

//Helping print function for XMILE tag data
fun printXMILETagDataInModels(map: MutableMap<String, Any>, names:List<String>){
    for(index in names.indices) {
        var name=names[index]
        println(name)
        println("" + (map.filterKeys { it.contains("Version") }))
        println("" + (map.filterKeys { it.contains("xmlns1") }))
        println("" + (map.filterKeys { it.contains("xmlnsISEE") })+ "\n")
    }

}

fun descriptionPackage(name: String,index: Int,names: List<String>,map: MutableMap<String, Any>, transString: MutableList<String>){
    transString[index] += "\n"+descriptionPackage
    transString[index] += " * XMILE version = "+map.getValue("Version")+"\n"
    transString[index] += " * xmlns = "+map.getValue("xmlns1")+"\n"
    transString[index] += " * xmlnsISEE = "+map.getValue("xmlnsISEE")+"\n"
    transString[index] += " * Model name = "+names[index].lowercase().replaceFirstChar { it.uppercase() }+"\n"
    transString[index] += " * Vendor = "+map.getValue("Vendor")+"\n"
    transString[index] += " * Product name = "+map.getValue("Product name")+"\n"
    transString[index] += " * Product version = "+map.getValue("Product version")+"\n*/\n\n"
    if (index==0){
    transString[index] += "\n" +
            "open class Model$name : Model() {\n" +
            "\n"}
    else if(index>=1){
        transString[index] += "\n" +
                "open class Module$name : Model() {\n" +
                "\n"
    }
}

fun creatingTransStringHelpFunction(tModel: MutableMap<String, Any>,i: Int,index: Int, entry: Any,token: String,
                                    tokens: String, transString: MutableList<String>,counter: MutableList<Int>,
                                    modelName: String ="non", moduleName: String = "non",){
    var constantsList = mutableListOf<String>()
    constantsList = checkEquation(tModel,constantsList,"constants")

    var stockList = mutableListOf<String>()
    stockList = checkEquation(tModel,stockList,"stocks")

    var globalSimSpecsVariable = listOf<String>("STARTTIME","STOPTIME","DT","TIME")

    var entry = entry

    for(constant in constantsList) {
        if(entry is String) {
            entry = entry.replace(constant, constant.uppercase())
        }
    }

    for(stocks in stockList){
        if(entry is String){
            entry = entry.replace(stocks,stocks.lowercase().replaceFirstChar { it.uppercase()})
        }

    }

    if (tokens == token && counter[i] == 0 && moduleName=="non"){
        counter[i]++
        transString[index] += "\n\t\t\t\t// $token\n"+"\t\t\t\t\t\t${entry} \n"
    }else if (tokens == token && moduleName=="non"){
        transString[index] += "\t\t\t\t\t\t${entry} \n"
    }else if (tokens+modelName+moduleName == token+modelName+moduleName && moduleName!="non"&& token=="Module imports"){
        transString[index] += "$entry \n"
    }else if (tokens+modelName+moduleName == token+modelName+moduleName && counter[i]==0 && moduleName!="non"){
        counter[i]++
        transString[index] += "\n\t\t\t\t// $token\n"+"\t\t\t\t\t\t${entry} \n"
    }
    else if (tokens+modelName+moduleName == token+modelName+moduleName && moduleName!="non"){
        transString[index] += "\t\t\t\t\t\t${entry} \n"
    }


}

fun transpiledModelsData(tModel: MutableMap<String, Any>){
    val modelNames = tModel.filterKeys { it.contains("model names") }
    var names = modelNames.values.toString().removeSurrounding("[[","]]").split(", ")
    //println(names)

    var tokens = listOf<String>("Module imports","companion object keys","companion object values", "companion object simSpec","2. Variables constants",
        "2. Variables converters","2. Stocks","2. Flows","2. Modules","2. Entities unit","2. Entities description",
        "3. Stocks", "4. Variables constant","4. Stocks","4. Flows", "4. Converters")
    var counter = mutableListOf<Int>(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    var transString = mutableListOf<String>()
    var name=""
    for(index in names.indices){
        name=names[index].lowercase().replaceFirstChar { it.uppercase()}
        transString.add(index, packageAndImports)

        var moduleName=tModel.filterKeys { it.contains("Module name $name") }.values
        moduleName=moduleName.toString().removeSurrounding("[","]").split(", ")
        for (j in moduleName.indices){
            for(i in tokens.indices) {
                for (entry in tModel.entries.iterator()) {
                    if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])) {
                        counter = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
                        creatingTransStringHelpFunction(tModel,i, index, entry.value, "Module imports", tokens[i], transString,
                        counter, names[index], moduleName[j]
                    )
                    }
                }
            }
        }

        descriptionPackage(name, index,names,tModel,transString)
        transString[index] += constantValuesBeginning

        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in tModel.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"companion object keys",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"companion object values",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"companion object simSpec",tokens[i],transString,counter)
                }
            }
        }
        transString[index]+="\n" + "\t\t\t}"
        transString[index] += initializationOfModelCreateAllSystemElements
        transString[index] += "\t"+tModel.getValue("Method "+ names[index])
        transString[index] += "\n\t"+tModel.getValue("Model name "+ names[index])
        transString[index] += "\n\t"+tModel.getValue("Time unit "+ names[index])
        transString[index] += createAllSystemElements
        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in tModel.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Variables constants",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Variables converters",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Stocks",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Flows",tokens[i],transString,counter)
                }
            }
        }
        for (j in moduleName.indices){
            for(i in tokens.indices){
                counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
                for (entry in tModel.entries.iterator()) {
                    if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                        creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Modules",tokens[i],transString,
                            counter,names[index],moduleName[j])
                    }
                }
            }
        }

        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in tModel.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Entities unit",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"2. Entities description",tokens[i],transString,counter)

                }
            }
        }
        transString[index] +=  initialValues
        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in tModel.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"3. Stocks",tokens[i],transString,counter)
                }
            }
        }
        transString[index] +=  equations
        for(i in tokens.indices){
            counter= mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            for (entry in tModel.entries.iterator()) {
                if(entry.key.contains(names[index]) && entry.key.contains(tokens[i])){
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"4. Variables constants",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"4. Stocks",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"4. Flows",tokens[i],transString,counter)
                    creatingTransStringHelpFunction(tModel,i,index,entry.value,"4. Converters",tokens[i],transString,counter)
                }
            }
        }
        transString[index]+="\n" +
                "\t\t\t}\n}"
    }
    println(transString)
}