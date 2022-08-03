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
    var xmlns = getWantedString(list,  "xmlns")
    var xmlns1 = getWantedString(list,  "xmlns:isee")

    /**
     * Returning the values in XMILE tag with data saved in map structure if every condition for XMILE tag is satisfied
     */

    if(versionXMILE=="" || xmlns ==""){

        return mapOf("Error" to "XMILE tag not properly configured!!!")
    }
    else{

        val XMILETagDataMap =mapOf("Version" to versionXMILE, "xmlns" to xmlns, "xmlnsISEE" to xmlns1)
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

        if (modelName.isEmpty() || modelVendor.isEmpty() || productName.isEmpty() || versionProduct == "" ){

            return mapOf("Error" to "Header tag not properly configured!!!")

        }else{
            var modelNameTxt = modelName[1]
            val modelVendorTxt = modelVendor[1]
            val productNameTxt = productName[1]

            /**
             * Returning the values in header tag with data saved in map structure
             */

            val HeaderTagDataMap =mapOf("Model name" to modelNameTxt, "Vendor" to modelVendorTxt, "Product name" to productNameTxt,"Product version" to versionProduct)


            return HeaderTagDataMap
        }

    }

    return mapOf("Error" to "Header tag not properly configured!!!")

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
fun helpTestUnitDes(tMap: MutableMap<String,Any>, name: String, units: MutableList<String>,
                    description: MutableList<String>, nonNegative: Boolean){
    if (units.isNotEmpty()){
        val value=units[1]
        tMap += mapOf("2. Entities unit" to "$name.unit =  \"$value\" ")
    }

    if (description.isNotEmpty()) {
        val valueDes = description[1]
        tMap +=
            mapOf(
                "2. Entities description" to "$name.description =  \"$valueDes\"" +
                        "Variable non negative status = \"$nonNegative\"" + ". "
            )
    }else{
        tMap +=
            mapOf(
                "2. Entities description" to "$name.description = "+
                        "Variables non negative status = \"$nonNegative\"" + ". "
            )
    }

}

fun gettingStocks(token: MutableList<String>,tModel: MutableMap<String,Any>, modelName: String,stockNameToken:String){
    val stockName = getWantedString(stockNameToken,  "name").lowercase().replaceFirstChar { it.uppercase() }

    if(stockNameToken.isNotEmpty()) {
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
            inflows = if (i == 0) {
                inflowsList[i][1]
            } else {
                inflows + " + " + inflowsList[i][1]
            }

        }
        for (j in outflowsList.indices) {
            outflows = outflows + " - " + outflowsList[j][1]
        }

        tModel +=mapOf("$modelName $stockName 2.Stocks" to "val $stockName = model.stock(\"$stockName\")")
        if (valueEquationToken.isNotEmpty()) {
            try {
                value = valueEquationToken[1].toDouble()
                val stockNameUp = stockName.uppercase()
                tModel += mapOf("$modelName $stockName companion object" to "const val $stockNameUp" + "_KEY = \"$stockNameUp\"")
                tModel += mapOf("$modelName $stockName companion object" to "const val $stockNameUp" + "_VALUE = $value")
                tModel += mapOf("$modelName $stockName 2. Variables" to "val $stockNameUp = model.constant($stockNameUp" + "_KEY)")
                tModel += mapOf("$modelName $stockName 4. Variables" to "val $stockNameUp = model.equation($stockNameUp" + "_VALUE)")
                tModel += mapOf("$modelName $stockName 3. Stocks" to "$stockName.initialValue={ $stockNameUp }")
                tModel += mapOf("$modelName $stockName 4. Stocks" to "$stockName.equation={$inflows$outflows}")
            } catch (e: Exception) {
                val stockNameLow = stockName.lowercase()
                val value1 = valueEquationToken[1]
                tModel += mapOf("$modelName $stockName 2. Variables->converter" to "val $stockNameLow = model.converter(\"$stockNameLow\")")
                tModel += mapOf("$modelName $stockName 4. Converters" to "$stockNameLow.equation={ $value1 }")
                tModel += mapOf("$modelName $stockName 3. Stocks" to "$stockName.initialValue={ $stockNameLow }")
                tModel += mapOf("$modelName $stockName 4. Stocks" to "$stockName.equation={$inflows$outflows}")
                tModel += mapOf("$modelName $stockName 4. Stocks" to "$stockName.equation={$inflows$outflows}")
            }
        } else {
            tModel +=
                mapOf(
                    "$modelName $stockName Error" to  "Value was not declared so this stock will not be used in simulation!!"
                )
        }

        helpTestUnitDes(tModel, stockName, units, description, nonNegative)
    }else{
        println("Error " + "Stock name must not be empty!!!")
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


        tModel += mapOf("$modelName $flowName 2. Flows" to "val $flowName = model.flow(\"$flowName\")")
        if (valueEquationToken.isNotEmpty()) {

                tModel += mapOf("$modelName $flowName 4. Flows" to "$flowName.equation={ $value }")

        } else {
            tModel +=
                mapOf(
                    "$modelName $flowName Error" to "Value was not declared so this stock will not be used in simulation!!"
                )

        }

        helpTestUnitDes(tModel, flowName, units, description, nonNegative)

        val gf = separateSameTags(token, "<gf", "</gf>")
        if (gf.isNotEmpty()) {
            gettingGf(gf[0],tModel)
        }
    }else{
        println("Error" to "Flow name must not be empty!!!")
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
                tModel += mapOf("$modelName $auxName companion object" to "const val $auxNameUp" + "_KEY = \"$auxNameUp\"")
                tModel += mapOf("$modelName $auxName companion object" to "const val $auxNameUp" + "_VALUE = $value")
            } catch (e: Exception) {
                val value1 = valueEquationToken[1]
                tModel += mapOf("$modelName $auxName 2. Variables->converter" to "val $auxName = model.converter(\"$auxName\")")
                tModel += mapOf("$modelName $auxName 4. Variables" to "$auxName.equation(\"$value1\")")

            }
        }
        helpTestUnitDes(tModel, auxName, units, description, nonNegative)

        val gf = separateSameTags(token, "<gf", "</gf>")
        if (gf.isNotEmpty()) {
            gettingGf(gf[0],tModel)
        }
    }else{
        println("Error " +"Aux name must not be empty!!!")
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

fun gettingModules(tokens: MutableList<String>, tModel: MutableMap<String,Any>){

    var connectionTo=""
    var connectionFrom=""
    var connectionToFrom= mutableListOf<Map<String,String>>()
    val moduleNameToken = getDataInTag(tokens, "<module")
    val moduleName = getWantedString(moduleNameToken, "name")
    val description = breakListToSubList(tokens, "<doc", "</doc>")
    val moduleConnectionList= getDataInTags(tokens,"<connect")


    tModel += mapOf("moduleName" to moduleName)
    if (description.isNotEmpty()){
        val value=description[1]
        tModel += mapOf("stock.description" to "$moduleName.description =  \"$value\" ")
    }

    for(index in moduleConnectionList.indices){
        connectionTo = getWantedString(moduleConnectionList[index],  "to")
        connectionFrom = getWantedString(moduleConnectionList[index], "from")
        connectionToFrom.add( mapOf(connectionTo to connectionFrom))
    }

    tModel += mapOf("connectionToFrom" to "$connectionToFrom")
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
            gettingModules(moduleList[index],tModel)
        }
    }

    return tModel
}

fun gettingModelTagData(tokens: MutableList<String>, rootModelName: String): MutableMap<String, Any> {

    val modelNameList = mutableListOf<String>()
    val modelList = separateSameTags(tokens, "<model", "</model>")
    var tModels = mutableMapOf<String,Any>()

    if (modelList.isEmpty()) {

        return mutableMapOf("Error" to "Model tag is not included in XMILE dokument!")

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
                return mutableMapOf("Error" to "Model or models beyond the root model are not properly named!")
            }
            modelNameList.add(name)

        }
    }
    tModels=gettingModelsVariables(modelList, modelNameList)
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
 * 7. MUST include, when using optional features, the <options> tag with those features specified (Section 2.2.1)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingOptionsTagData(tokens: MutableList<String>):Map<String, Any>{
    val optionslList = breakListToSubList(tokens, "<options", "</options>")
    //println(optionslList)
    //val usesSubModels = getLowerLevelOfList(tokens, "<options", "</options>")
    // println(usesSubModels)
    return mapOf("" to "")
}


/**
 * XMILE file Base-Level Conformance
 * 8. MUST contain at least one set of simulation specifications (Section 2.3)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingSimSpecsTagData(tokens: MutableList<String>): Map<String, Any> {

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format

    val simSpecsList = breakListToSubList(tokens,"<sim_specs","</sim_specs>")
    if(simSpecsList.isNotEmpty()) {
        val startOfInterval = breakListToSubList(simSpecsList, "<start>", "</start>")
        val endOfInterval = breakListToSubList(simSpecsList, "<stop>", "</stop>")
        var interval = breakListToSubList(simSpecsList, "<dt>", "</dt>")
        val simSpecsTopString = getDataInTag(tokens, "<sim_specs")
        var methodSD = getWantedString(simSpecsTopString,  "method")
        var timeUnitSD = getWantedString(simSpecsTopString,  "time_units")
        val a = startOfInterval[1].toDoubleOrNull()
        val b = endOfInterval[1].toDoubleOrNull()
        val c = interval[1].toDoubleOrNull()

        if (startOfInterval.isEmpty() || endOfInterval.isEmpty() || a == null || b == null) {

            return mapOf("Error" to "Sim_specs start or stop tag not properly configured!!!")

        } else if (c == null || interval.isEmpty() && (a != null && b != null)) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = 1.0

            val SimSpecsTagDataMap = mapOf(
                "Method" to methodSD, "Time unit" to timeUnitSD, "Initial time" to initialTime,
                "Final time" to finalTime, "Time step" to timeStep
            )

            return SimSpecsTagDataMap

        } else if (a != null && b != null && c != null) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = interval[1].toDouble()


            val SimSpecsTagDataMap = mapOf(
                "Method" to methodSD, "Time unit" to timeUnitSD, "Initial time" to initialTime,
                "Final time" to finalTime, "Time step" to timeStep
            )


            return SimSpecsTagDataMap
        }else{
            return mapOf("Error" to "Sim_specs tag not properly configured!!!")
        }
    }
    return mapOf("Error" to "Sim_specs tag is not included in XMILE dokument!")

    /**
     * Working ++ (tested and confirmed rules with Test12, Test13 and Test14)
     * But resolving inconsistencies between multiple sim_specs needs to be created.
     */
}

/**
 * XMILE file
 * 9. MUST support model behaviors (Section 2.6)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */
fun gettingBehaviorTagData(tokens: MutableList<String>):String{

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val behavior = breakListToSubList(tokens,"<behavior","</behavior>")
    //println(behavior)

    var list= ""
    return list
}


