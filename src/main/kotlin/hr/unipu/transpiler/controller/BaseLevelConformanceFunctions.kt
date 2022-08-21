package hr.unipu.transpiler.controller

/**
 * XMILE file Base-Level Conformance
 * 1. MUST include an <xmile> tag that contains both the version of XMILE used and the XMILE XML namespace (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingXMILETagData(tokens: MutableList<String>): Map<String, Any> {
    //hr.unipu.transpiler.XMILE tokens (tags) only version  ( version 1.0)

    val list = getDataInTag(tokens, "<xmile")
    //println(list)
    var versionXMILE = getWantedString(list,  "version")
    var xmlns = getWantedStringXMILETag(list,  "xmlns")
    var xmlns1 = getWantedStringXMILETag(list,  "xmlns:isee")

    /**
     * Returning the values in XMILE tag with data saved in map structure if every condition for XMILE tag is satisfied
     */

    if(versionXMILE=="" || xmlns ==""){

        error("Error: XMILE tag not properly configured!!! 1. MUST include an <xmile> tag that contains both the " +
                "version of XMILE used and the XMILE XML namespace (Section 2)")
    }
    else{

        val XMILETagDataMap =mapOf("Version" to versionXMILE, "xmlns1" to xmlns, "xmlnsISEE" to xmlns1)
        return XMILETagDataMap
    }
    /**
     * Working ++ (tested and confirmed rules with Test6, Test7 and Test8)
     */
}

/**
 * XMILE file Base-Level Conformance
 * 2. MUST include a <header> tag (Section 2) with sub-tags <vendor> and <product> with its version number (Section 2.2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingHeaderTagData(tokens: MutableList<String>): Map<String, Any> {

    val headerList=breakListToSubList(tokens,"<header","</header>")
    //println(headerList)
    if(headerList.isNotEmpty()) {
        val modelName = breakListToSubList(headerList, "<name", "</name>")
        val modelVendor = breakListToSubList(headerList, "<vendor", "</vendor>")
        val productName = breakListToSubList(headerList, "<product", "</product>")
        val modelProductList = getDataInTag(headerList, "<product")
        var versionProduct = getWantedString(modelProductList,  "version")
        val subModelOptionsBool= gettingOptionsTagData(headerList)

        if (modelName.isEmpty() || modelVendor.isEmpty() || productName.isEmpty() || versionProduct == "" ){

            error("Error: Header tag not properly configured!!! 2. MUST include a <header> tag (Section 2) with " +
                    "sub-tags <vendor> and <product> with its version number (Section 2.2)")

        }else{
            var modelNameTxt = modelName[1]
            val modelVendorTxt = modelVendor[1]
            val productNameTxt = productName[1]

            /**
             * Returning the values in header tag with data saved in map structure
             */

            val HeaderTagDataMap =mapOf("Model name" to modelNameTxt, "Vendor" to modelVendorTxt,
                "Product name" to productNameTxt,"Product version" to versionProduct,
                "Options SubModel" to subModelOptionsBool)


            return HeaderTagDataMap
        }

    }

    error ("Error: Header tag not properly configured!!!")

    /**
     * Working ++ (tested and confirmed rules with Test9, Test10 and Test11)
     */
}

/**
 * XMILE file Base-Level Conformance
 * 3. MUST include at least one <model> tag (Section 2)
 * 4. MUST name models beyond the root model (Section 4)
 * 11.  MUST support all base functionality objects (Section 3.1 and all subsections)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

/**
 *  Group of functions that are getting data about model variables and returning it to function gettingModelVariables
 *  as data tip of MutableList of Map
 *  Functions:
 *             gettingStocks (Section 3.1.1)
 *             gettingFlows (Section 3.1.2)
 *             gettingAux (Section 3.1.3)
 *             gettingGr  (Section 3.1.4)
 *             gettingGroup (Section 3.1.5)
 *             gettingModules (Section 3.1.5)
 */
fun helpTestUnitDes(tMap: MutableMap<String,Any>,modelName: String, name: String, units: MutableList<String>,
                    description: MutableList<String>, nonNegative: Boolean){
    if (units.isNotEmpty()){
        val value=units[1]
        tMap += mapOf("$modelName $name 2. Entities unit" to "$name.unit =  \"$value\" ")
    }

    if (description.isNotEmpty()) {
        val valueDes = description[1]
        tMap +=
            mapOf(
                "$modelName $name 2. Entities description" to "$name.description = \"$valueDes\"" +
                        "\t//Variable non negative status is $nonNegative"
            )
    }
    /**else{
    tMap +=
    mapOf(
    "$modelName $name 2. Entities description" to "$name.description = \"" +
    "Variable non negative status is $nonNegative" + ".\" "
    )
    }*/

}

/**fun checkEquationConstants(tModel: MutableMap<String,Any>, constantsList: MutableList<String>): MutableList<String>{
    var constantsList=constantsList
    if(tModel.contains("constants")) {
        val constantsStr = tModel.getValue("constants")
        if(constantsStr is String) {
            constantsList = constantsStr.split(",").toMutableList()
        }
    } else {
        constantsList = mutableListOf<String>()
    }
    return constantsList

}*/

fun checkEquation(tModel: MutableMap<String,Any>, list: MutableList<String>,nameKey: String): MutableList<String>{

    var list=list
    if(tModel.contains("$nameKey")) {
        val str = tModel.getValue("$nameKey")
        if(str is String) {
            list = str.split(",").toMutableList()
        }
    } else {
        list = mutableListOf<String>()
    }
    return list
}

fun checkBuiltInFunctions(tModel: MutableMap<String,Any>,list: MutableList<String>,nameKey:String): MutableList<String>{

    var list=list
    var listBuiltInFunctions = listOf<String>("STARTTIME","STOPTIME","DT","TIME")


    for(i in listBuiltInFunctions){
        if (tModel.contains("$nameKey")){

            }
    }
    return list

}
fun gettingStocks(token: MutableList<String>,tModel: MutableMap<String,Any>, modelName: String,stockNameToken:String){
    val stockName = getWantedString(stockNameToken,  "name").lowercase().replaceFirstChar { it.uppercase()}
    val stockName1 = getWantedString(stockNameToken, "name")
    if(stockName.isNotEmpty()) {

        val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
        val inflowsList = separateSameTags(token, "<inflow", "</inflow>")
        val outflowsList = separateSameTags(token, "<outflow", "</outflow>")
        val units = breakListToSubList(token, "<units", "</units>")
        val description = breakListToSubList(token, "<doc", "</doc>")
        val nonNegativeOne = token.contains("<non_negative/>")
        val nonNegativeTwo = token.contains("</non_negative>")
        val nonNegative: Boolean = nonNegativeOne || nonNegativeTwo
        var inflows = ""
        var outflows = ""
        var value = 0.0

        for (i in inflowsList.indices) {
            var x = inflowsList[i][1]
            var constantsList = mutableListOf<String>()
            constantsList = checkEquation(tModel,constantsList,"constants")
            if(constantsList.contains(x.lowercase())) {
                x = x.uppercase()
            }
            inflows = if (i == 0) {
                x
            } else {
                inflows + " + (" + x
            }


        }
        if(inflowsList.size>0) {
            inflows += ")".repeat(inflowsList.size - 1)
        }
        for (i in outflowsList.indices) {
            var x = outflowsList[i][1]

            var constantsList = mutableListOf<String>()
            constantsList = checkEquation(tModel,constantsList,"constants")

            if(constantsList.contains(x.lowercase())) {
                x = x.uppercase()
            }
            outflows =if (i == 0) {
                " - ("+x
            } else {
                outflows+  " + (" + x
            }
        }

        outflows+=")".repeat(outflowsList.size)

        tModel +=mapOf("$modelName $stockName 2. Stocks" to "val $stockName = model.stock(\"$stockName\")")
        if (valueEquationToken.isNotEmpty()) {
            try {
                value = valueEquationToken[1].toDouble()
                val stockNameUp = stockName.uppercase()

                var constantsList = mutableListOf<String>()
                constantsList = checkEquation(tModel,constantsList,"constants")
                constantsList.add(stockName.lowercase())

                var stockList = mutableListOf<String>()
                stockList = checkEquation(tModel,stockList,"stocks")
                stockList.add(stockName1)

                var simSpecsList = mutableListOf<String>()
                simSpecsList= checkEquation(tModel,simSpecsList,"simSpecsEq")


                tModel.put("constants", constantsList.joinToString(","))
                tModel.put("stocks", stockList.joinToString (","))
                tModel.put("simSpecsEq", simSpecsList.joinToString (","))

                tModel += mapOf("$modelName $stockName companion object keys" to "const val $stockNameUp" + "_KEY = \"$stockNameUp\"")
                tModel += mapOf("$modelName $stockName companion object values" to "const val $stockNameUp" + "_VALUE = $value")
                tModel += mapOf("$modelName $stockName 2. Variables constants" to "val $stockNameUp = model.constant($stockNameUp" + "_KEY)")
                tModel += mapOf("$modelName $stockName 4. Variables constants" to "$stockNameUp = model.equation($stockNameUp" + "_VALUE)")
                tModel += mapOf("$modelName $stockName 3. Stocks" to "$stockName.initialValue={ $stockNameUp }")
                tModel += mapOf("$modelName $stockName 4. Stocks" to "$stockName.equation={$inflows$outflows}")
                helpTestUnitDes(tModel, modelName, stockNameUp, units, description, nonNegative)
            } catch (e: Exception) {
                val stockNameLow = stockName.lowercase()
                val value1 = valueEquationToken[1]
                var stockList = mutableListOf<String>()
                stockList = checkEquation(tModel,stockList,"stocks")
                stockList.add(stockName1)

                tModel.put("stocks", stockList.joinToString (","))

                tModel += mapOf("$modelName $stockName 2. Variables converters" to "val $stockNameLow = model.converter(\"$stockNameLow\")")
                tModel += mapOf("$modelName $stockName 4. Converters" to "$stockNameLow.equation={ $value1 }")
                tModel += mapOf("$modelName $stockName 3. Stocks" to "$stockName.initialValue={ $stockNameLow }")
                tModel += mapOf("$modelName $stockName 4. Stocks" to "$stockName.equation={$inflows$outflows}")
                helpTestUnitDes(tModel, modelName, stockNameLow, units, description, nonNegative)
            }
        } else {
            tModel +=
                mapOf(
                    "$modelName $stockName Error" to  "Value was not declared so this stock will not be used in simulation!!"
                )
        }

    }else{
        error("Error: Stock name must not be empty!!!")
    }
}

fun gettingFlows(token: MutableList<String>,tModel: MutableMap<String,Any>, modelName: String, flowNameToken: String){

    val flowName = getWantedString(flowNameToken, "name").lowercase().replace(' ', '_')

    if(flowName.isNotEmpty()) {

        val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
        val units = breakListToSubList(token, "<units", "</units>")
        val description = breakListToSubList(token, "<doc", "</doc>")
        val nonNegativeOne = token.contains("<non_negative/>")
        val nonNegativeTwo = token.contains("</non_negative>")
        val nonNegative: Boolean = nonNegativeOne || nonNegativeTwo
        var value = 0.0
        var valueStr=""

        tModel += mapOf("$modelName $flowName 2. Flows" to "val $flowName = model.flow(\"$flowName\")")
        if (valueEquationToken.isNotEmpty()) {

            valueStr=valueEquationToken[1]
            var constantsList = mutableListOf<String>()
            constantsList= checkEquation(tModel,constantsList,"constants")
            for(constant in constantsList) {
                valueStr = valueStr.replace(constant, constant.uppercase())
            }

            tModel += mapOf("$modelName $flowName 4. Flows" to "$flowName.equation={ $valueStr }")

        } else {
            tModel +=
                mapOf(
                    "$modelName $flowName Error" to "Value was not declared so this stock will not be used in simulation!!"
                )

        }

        helpTestUnitDes(tModel, modelName, flowName, units, description, nonNegative)

        val gf = separateSameTags(token, "<gf", "</gf>")
        if (gf.isNotEmpty()) {
            gettingGf(gf[0],tModel)
        }
    }else{
        error("Error: Flow name must not be empty!!!")
    }


}

fun gettingAux(token: MutableList<String>, tModel: MutableMap<String, Any>, modelName: String, auxNameToken:String){

    val auxName = getWantedString(auxNameToken, "name").lowercase()

    if(auxName.isNotEmpty()) {

        val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
        val units = breakListToSubList(token, "<units", "</units>")
        val description = breakListToSubList(token, "<doc", "</doc>")
        val nonNegativeOne = token.contains("<non_negative/>")
        val nonNegativeTwo = token.contains("</non_negative>")
        val nonNegative: Boolean = nonNegativeOne || nonNegativeTwo
        var value = 0.0


        if (valueEquationToken.isNotEmpty()) {
            try {
                value = valueEquationToken[1].toDouble()
                val auxNameUp = auxName.uppercase()
                //tModel += mapOf("upercase" to stockName)
                var constantsList = mutableListOf<String>()
                constantsList= checkEquation(tModel,constantsList,"constants")
                constantsList.add(auxName.lowercase())
                tModel.put("constants", constantsList.joinToString(","))
                tModel += mapOf("$modelName $auxName companion object keys" to "const val $auxNameUp" + "_KEY = \"$auxNameUp\"")
                tModel += mapOf("$modelName $auxName companion object values" to "const val $auxNameUp" + "_VALUE = $value")
                tModel += mapOf("$modelName $auxName 2. Variables constants" to "val $auxNameUp = model.constant($auxNameUp" + "_KEY)")
                tModel += mapOf("$modelName $auxName 4. Variables constants" to "$auxNameUp = model.equation($auxNameUp" + "_VALUE)")
                helpTestUnitDes(tModel, modelName, auxNameUp, units, description, nonNegative)
            } catch (e: Exception) {
                var value1 = valueEquationToken[1]
                tModel += mapOf("$modelName $auxName 2. Variables converters" to "val $auxName = model.converter(\"$auxName\")")
                var constantsList = mutableListOf<String>()
                constantsList= checkEquation(tModel,constantsList,"constants")
                for(constant in constantsList) {
                    value1 = value1.replace(constant, constant.uppercase())
                }
                tModel += mapOf("$modelName $auxName 4. Variables" to "$auxName.equation(\"$value1\")")
                helpTestUnitDes(tModel, modelName, auxName, units, description, nonNegative)
            }
        }

        val gf = separateSameTags(token, "<gf", "</gf>")
        if (gf.isNotEmpty()) {
            gettingGf(gf[0],tModel)
        }
    }else{
        error("Error: Aux name must not be empty!!!")
    }

}

fun gettingGf(token: MutableList<String>, tModel: MutableMap<String,Any>,gfNameToken: String ="non"){
    val gfName = getWantedString(gfNameToken,  "name")
    if (gfName == ""  && gfNameToken != "non"){
        tModel += ("Error" to
                "If graphs is outside aux or flow tag it must contained attribute name!!!")
    }
    if (gfNameToken != "non"){
        tModel += mapOf("gfName" to gfName)
    }
    val xScale = getDataInTag(token,"<xscale")
    val yScale = getDataInTag(token,"<yscale")
    val xMin = getWantedString(xScale,  "min")
    val xMax = getWantedString(xScale,  "max")
    val yMin = getWantedString(yScale,  "min")
    val yMax = getWantedString(yScale, "max")
    val xPts= breakListToSubList(token,"<xpts","</xpts>")
    val yPts= breakListToSubList(token,"<ypts","</ypts>")
    val description = breakListToSubList(token, "<doc", "</doc>")

    tModel += mapOf("xMin" to xMin)
    tModel += mapOf("xMax" to xMax)
    tModel += mapOf("yMin" to yMin)
    tModel += mapOf("yMax" to yMax)

    if(xPts.isNotEmpty()){
        tModel += mapOf("xPts" to xPts[1])
    }else{
        tModel += mapOf("xPts" to "empty")
    }
    if(yPts.isNotEmpty()){
        tModel += mapOf("yPts" to yPts[1])
    }else{
        tModel += mapOf("yPts" to "empty")
    }
    if (description.isNotEmpty()){
        val value=description[1]
        tModel += mapOf("gf.description" to "description =  \"$value\" ")
    }
}

fun gettingGroup(token: MutableList<String>, tModel: MutableMap<String,Any>, groupNameToken: String){

    val groupName = getWantedString(groupNameToken, "name")
    val run =  getWantedString(groupNameToken,"run")
    val description = breakListToSubList(token, "<doc", "</doc>")
    var entityNameList = mutableListOf<String>()

    tModel += mapOf("groupName" to groupName)
    tModel += mapOf("run" to run)

    val groupEntityList = getDataInTags(token,"<entity")
    if (description.isNotEmpty()){
        val value=description[1]
        tModel += mapOf("stock.description" to "$groupName.description =  \"$value\" ")
    }

    for (index in groupEntityList.indices){
        entityNameList.add(getWantedString(groupEntityList[index], "name"))
    }

    tModel += mapOf("entityNameList" to "$entityNameList")

}

fun gettingModules(tokens: MutableList<String>, modelName: String, tModel: MutableMap<String,Any>){

    var connectionTo=""
    var connectionFrom=""
    val moduleNameToken = getDataInTag(tokens, "<module")
    var moduleName = getWantedString(moduleNameToken, "name")
    val description = breakListToSubList(tokens, "<doc", "</doc>")
    val moduleConnectionList= getDataInTags(tokens,"<connect")

    moduleName = moduleName.lowercase().replaceFirstChar { it.uppercase()}
    tModel += mapOf("Module name $modelName $moduleName" to moduleName)
    tModel += mapOf("Module imports $modelName $moduleName" to "import hr.unipu.ksdtoolkit.modules.Module$moduleName")
    tModel += mapOf("2. Modules $modelName $moduleName" to "\n\t\t\t\t\t\tval $moduleName = model.createModule(\n" +
            "\t\t\t\t\t\t\t\"$moduleName\",\n" +
            "\t\t\t\t\t\t\t\"hr.unipu.ksdtoolkit.modules.Module$moduleName\"\n" +
            "\t\t\t\t\t\t) as Module$moduleName")

    if (description.isNotEmpty()){
        val value=description[1]
        tModel += mapOf("$modelName $moduleName 2. Entities description" to "$moduleName.description =  \"$value\" ")
    }

    for(index in moduleConnectionList.indices){
        connectionTo = getWantedString(moduleConnectionList[index],  "to")
        connectionFrom = getWantedString(moduleConnectionList[index], "from")
    }

    tModel += mapOf("connection to $modelName $moduleName" to "$connectionTo")
    tModel += mapOf("connection from $modelName $moduleName" to "$connectionFrom")
}

fun gettingModelSimSpecs(tokens: MutableList<MutableList<String>>,modelNameList: MutableList<String>,simSpecMap: MutableMap<String, Any>):
        MutableMap<String,Any>{
    var mapSimSpecs = mutableMapOf<String, Any>()
    var name = ""

    for (index in tokens.indices){
        mapSimSpecs += gettingSimSpecsTagData(tokens[index],modelNameList[index])
        name=modelNameList[index]

        if(mapSimSpecs.contains("SimSpecs empty $name")){
            mapSimSpecs.remove("SimSpecs empty $name")
            mapSimSpecs += "Model name $name" to "model.name = \"$name\"\t// name is optional"
            mapSimSpecs += "Method $name" to ""+simSpecMap.getValue("Method non")
            mapSimSpecs += "Time unit $name" to ""+simSpecMap.getValue("Time unit non")
            mapSimSpecs += "companion object simSpec Initial time $name" to ""+
                    simSpecMap.getValue("companion object simSpec Initial time non")
            mapSimSpecs += "companion object simSpec Final time $name" to ""+
                    simSpecMap.getValue("companion object simSpec Final time non")
            mapSimSpecs += "companion object simSpec Time step $name" to ""+
                    simSpecMap.getValue("companion object simSpec Time step non")
        }
    }
    //println(mapSimSpecs)
    return mapSimSpecs
}

fun gettingModelsVariables(tokens: MutableList<MutableList<String>>,modelNameList: MutableList<String>): MutableMap<String,Any>{
    var tModel= mutableMapOf<String,Any>()

    for(i in tokens.indices){
        val stockList = separateSameTags(tokens[i],"<stock","</stock>")
        val stockNameToken = getDataInTags(tokens[i], "<stock")
        for((index)in stockList.withIndex()){
            gettingStocks(stockList[index],tModel,modelNameList[i],stockNameToken[index])

        }
    }

    for(i in tokens.indices){
        val flowList = separateSameTags(tokens[i],"<flow","</flow>")
        val flowNameToken = getDataInTags(tokens[i], "<flow")
        for((index)in flowList.withIndex()){
            gettingFlows(flowList[index],tModel,modelNameList[i],flowNameToken[index])
        }
    }

    for(i in tokens.indices){
        val auxList = separateSameTags(tokens[i],"<aux","</aux>")
        val auxNameToken = getDataInTags(tokens[i], "<aux")
        for((index)in auxList.withIndex()){
            gettingAux(auxList[index],tModel,modelNameList[i],auxNameToken[index])

        }
    }

    for(i in tokens.indices){
        val gfList = separateSameTags(tokens[i],"<gf","</gf>")
        val gfNameToken = getDataInTags(tokens[i], "<gf")
        for((index)in gfList.withIndex()){
            gettingGf(gfList[index],tModel,gfNameToken[index])
        }
    }

    for(i in tokens.indices){
        val groupList = separateSameTags(tokens[i],"<group","</group>")
        val groupNameToken = getDataInTags(tokens[i], "<group")
        for((index)in groupList.withIndex()){
            gettingGroup(groupList[index],tModel,groupNameToken[index])
        }
    }

    for(i in tokens.indices){
        val moduleList = separateSameTags(tokens[i], "<module", "</module>")
        for((index) in moduleList.withIndex()){
            gettingModules(moduleList[index],modelNameList[i],tModel)
        }
    }

    return tModel
}

fun gettingModelTagData(tokens: MutableList<String>, rootModelName: String, simSpecMap: MutableMap<String,Any>): MutableMap<String, Any> {

    val modelNameList = mutableListOf<String>()
    val modelList = separateSameTags(tokens, "<model", "</model>")
    var tModels = mutableMapOf<String,Any>()

    if (modelList.isEmpty()) {

        error("Error: Model tag is not included in XMILE dokument!")

    }

    for ((index) in modelList.withIndex()) {
        if (index == 0) {
            //println(modelList[index])
            val rootModel = getDataInTag(modelList[index], "<model")
            var nameRouteModel = getWantedString(rootModel,  "name")

            if (nameRouteModel == "") {

                nameRouteModel = rootModelName
                modelNameList.add(nameRouteModel)
                //println(nameRouteModel)

            }
        } else {

            val rootModel = getDataInTag(modelList[index], "<model")
            val name = getWantedString(rootModel,  "name")
            if (name == "") {
                error("Error: Model or models beyond the root model are not properly named!")
            }
            modelNameList.add(name)

        }
    }
    tModels = gettingModelsVariables(modelList, modelNameList)
    tModels += gettingModelSimSpecs(modelList,modelNameList,simSpecMap)
    tModels["model names"]=modelNameList
    return tModels

    /**
     * Working Base-Level Conformance: 3. MUST include at least one <model> tag (Section 2) (confirmed with test4)
     *                                 4. MUST name models beyond the root model (Section 4) (confirmed with test17)
     *                                 11.  MUST support all base functionality objects (Section 3.1 and all subsections)
     */
}


/**
 * XMILE file Base-Level Conformance
 * 6. MUST obey the namespace rules (Section 2.1 and 2.2.1)
 * 7. MUST include, when using optional features, the <options> tag with those features specified (Section 2.2.1)
 * Only optional feature that ksdtoolkit uses is feature sub_models thus it is the only one that needs to be implemented
 * in Base-Level Conformance 7.
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingOptionsTagData(tokens: MutableList<String>): Boolean {
    val optionslList = breakListToSubList(tokens, "<options", "</options>")
    return optionslList.contains("<uses_submodels/>")
}


/**
 * XMILE file Base-Level Conformance
 * 8. MUST contain at least one set of simulation specifications (Section 2.3)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingSimSpecsTagData(tokens: MutableList<String>,modelName: String = "non"): MutableMap<String, Any> {

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format

    val simSpecsList = breakListToSubList(tokens,"<sim_specs","</sim_specs>")
    if(simSpecsList.isNotEmpty()) {
        var simSpecsTagDataMap = mutableMapOf<String,Any>()
        val startOfInterval = breakListToSubList(simSpecsList, "<start>", "</start>")
        val endOfInterval = breakListToSubList(simSpecsList, "<stop>", "</stop>")
        var interval = breakListToSubList(simSpecsList, "<dt>", "</dt>")
        val simSpecsTopString = getDataInTag(tokens, "<sim_specs")
        var methodSD = getWantedString(simSpecsTopString,  "method")
        var timeUnitSD = getWantedString(simSpecsTopString,  "time_units")
        val a = startOfInterval[1].toDoubleOrNull()
        val b = endOfInterval[1].toDoubleOrNull()
        val c = interval[1].toDoubleOrNull()

        if ((startOfInterval.isEmpty() || endOfInterval.isEmpty() || a == null || b == null) && modelName=="non") {

            error("Error: Sim_specs start or stop tag not properly configured!!! 8. MUST contain at least one set of " +
                    "simulation specifications (Section 2.3)")

        } else if (c == null || interval.isEmpty() && (a != null && b != null)) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = 1.0

            if(methodSD.lowercase().contains("euler")){
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType = EulerIntegration()"
            }else if(methodSD.lowercase().contains("runge")||methodSD.lowercase().contains("kutta")){
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType =  RungeKuttaIntegration()"
            }else{
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType = EulerIntegration()\t//EulerIntegration is default integration"
            }
            simSpecsTagDataMap += "Model name $modelName" to "model.name = \"$modelName\"\t// name is optional"
            simSpecsTagDataMap += "Time unit $modelName" to "model.timeUnit = \"$timeUnitSD\"\t// unit is optional"
            simSpecsTagDataMap += "companion object simSpec Initial time $modelName" to "const val INITIAL_TIME_VALUE = $initialTime"
            simSpecsTagDataMap += "companion object simSpec Final time $modelName" to "const val FINAL_TIME_VALUE = $finalTime"
            simSpecsTagDataMap += "companion object simSpec Time step $modelName" to "const val TIME_STEP_VALUE = $timeStep"


            return simSpecsTagDataMap

        } else if (a != null && b != null && c != null) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = interval[1].toDouble()

            if(methodSD.lowercase().contains("euler")){
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType = EulerIntegration()"
            }else if(methodSD.lowercase().contains("runge")||methodSD.lowercase().contains("kutta")){
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType =  RungeKuttaIntegration()"
            }else{
                simSpecsTagDataMap += "Method $modelName" to "model.integrationType = EulerIntegration()\t//EulerIntegration is default integration"
            }

            simSpecsTagDataMap += "Model name $modelName" to "model.name = \"$modelName\"\t// name is optional"
            simSpecsTagDataMap += "Time unit $modelName" to "model.timeUnit = \"$timeUnitSD\"\t// unit is optional"
            simSpecsTagDataMap += "companion object simSpec Initial time $modelName" to "const val INITIAL_TIME_VALUE = $initialTime"
            simSpecsTagDataMap += "companion object simSpec Final time $modelName" to "const val FINAL_TIME_VALUE = $finalTime"
            simSpecsTagDataMap += "companion object simSpec Time step $modelName" to "const val TIME_STEP_VALUE = $timeStep"


            return simSpecsTagDataMap

        }else if(modelName=="non"){
            error("Error: Sim_specs tag not properly configured!!! 8. MUST contain at least one set of simulation " +
                    "specifications (Section 2.3)")
        }
    }else if(modelName=="non") {
        error("Error: Sim_specs tag is not included in XMILE dokument!")
    }
    return mutableMapOf("SimSpecs empty $modelName" to modelName)
    /**
     * Working ++ (tested and confirmed rules with Test12, Test13 and Test14)
     * Created resolving inconsistencies between multiple sim_specs ++
     */
}

/**
 * XMILE file
 * 9. MUST support model behaviors (Section 2.6)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */
fun gettingBehaviorTagData(tokens: MutableList<String>):String{

    //Getting data from behavior of hr.unipu.transpiler.XMILE format
    val behavior = breakListToSubList(tokens,"<behavior","</behavior>")
    //println(behavior)

    var list= ""
    return list
}


