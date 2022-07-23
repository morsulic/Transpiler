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
    var versionXMILE = getWantedString(list, ' ', "version")
    var xmlns = getWantedString(list, ' ', "xmlns")
    var xmlns1 = getWantedString(list, ' ', "xmlns:isee")

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
        var versionProduct = getWantedString(modelProductList, ' ', "version")

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



fun gettingStocks(token: MutableList<String>,modelName: String, stockNameToken:String):MutableList<Map<String,Any>>{

    var tStock= mutableListOf<Map<String,Any>>()



    val stockName = getWantedString(stockNameToken, ' ', "name")
    val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
    val inflowsList = separateSameTags(token,"<inflow", "</inflow>")
    val outflowsList = separateSameTags(token, "<outflow", "</outflow>")
    val units = breakListToSubList(token, "<units", "</units>")
    val nonNegative = token.contains("</non_negative>")
    val inflows = mutableListOf<String>()
    val outflows = mutableListOf<String>()

    for(i in inflowsList.indices){
        inflows.add(inflowsList[i][1])

    }
    for(j in outflowsList.indices){
        outflows.add(outflowsList[j][1])
    }

    tStock.add(mapOf("modelName" to modelName))
    tStock.add(mapOf("stockName" to stockName))
    if(valueEquationToken.isNotEmpty()){
        tStock.add(mapOf("initialValueEquation" to valueEquationToken[1]))
    }else{
        tStock.add(mapOf("initialValueEquation" to "empty"))
    }
    tStock.add(mapOf("inflows" to inflows))
    tStock.add(mapOf("outflows" to outflows))
    if (units.isNotEmpty()){
        tStock.add(mapOf("units" to units[1]))}
    else{
        tStock.add(mapOf("units" to "unit"))
    }
    tStock.add(mapOf("nonNegative" to nonNegative))


    return tStock

}

fun gettingFlows(token: MutableList<String>,modelName: String,flowNameToken: String): MutableList<Map<String,Any>>{

    var tFlow= mutableListOf<Map<String,Any>>()
    val flowName = getWantedString(flowNameToken, ' ', "name")
    val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
    val units = breakListToSubList(token, "<units", "</units>")
    val nonNegative = token.contains("</non_negative>")


    tFlow.add(mapOf("modelName" to modelName))
    tFlow.add(mapOf("flowName" to flowName))
    if(valueEquationToken.isNotEmpty()){
        tFlow.add(mapOf("initialValueEquation" to valueEquationToken[1]))
    }else{
        tFlow.add(mapOf("initialValueEquation" to "empty"))
    }
    if (units.isNotEmpty()){
        tFlow.add(mapOf("units" to units[1]))}
    else{
        tFlow.add(mapOf("units" to "unit"))
    }
    tFlow.add(mapOf("nonNegative" to nonNegative))

    val gf = separateSameTags(token, "<gf", "</gf>")
    if(gf.isNotEmpty()){
        var tGf=gettingGf(gf[0])
        tFlow.add(mapOf("graphs" to tGf))
    }

    return tFlow
}

fun gettingAux(token: MutableList<String>,modelName: String,auxNameToken:String): MutableList<Map<String,Any>>{
    var tAux= mutableListOf<Map<String,Any>>()
    val auxName = getWantedString(auxNameToken, ' ', "name")
    val auxAccess= getWantedString(auxNameToken,  ' ', "access")
    val valueEquationToken = breakListToSubList(token, "<eqn", "</eqn>")
    val units = breakListToSubList(token, "<units", "</units>")
    val nonNegative = token.contains("</non_negative>")

    tAux.add(mapOf("modelName" to modelName))
    tAux.add(mapOf("auxName" to auxName))
    tAux.add(mapOf("auxAccess" to auxAccess))
    if(valueEquationToken.isNotEmpty()){
        tAux.add(mapOf("initialValueEquation" to valueEquationToken[1]))
    }else{
        tAux.add(mapOf("initialValueEquation" to "empty"))
    }
    if (units.isNotEmpty()){
        tAux.add(mapOf("units" to units[1]))}
    else{
        tAux.add(mapOf("units" to "unit"))
    }
    tAux.add(mapOf("nonNegative" to nonNegative))

    val gf = separateSameTags(token, "<gf", "</gf>")
    if(gf.isNotEmpty()){
       var tGf=gettingGf(gf[0])
       tAux.add(mapOf("graphs" to tGf))
    }

    return tAux
}

fun gettingGf(token: MutableList<String>,modelName: String = "non", gfNameToken: String ="non"):MutableList<Map<String,Any>>{
    var tGf= mutableListOf<Map<String,Any>>()
    val gfName = getWantedString(gfNameToken, ' ', "name")
    if (gfName == "" && modelName != "non" && gfNameToken != "non"){
        return mutableListOf(mapOf("There is non model grafs standing alone in model section." to
                "but there could be in aux and flow section!!!"))
    }
    if (modelName != "non" && gfNameToken != "non"){
        tGf.add(mapOf("modelName" to modelName))
        tGf.add(mapOf("gfName" to gfName))
    }
    val xScale = getDataInTag(token,"<xscale")
    val yScale = getDataInTag(token,"<yscale")
    val xMin = getWantedString(xScale, ' ', "min")
    val xMax = getWantedString(xScale, ' ', "max")
    val yMin = getWantedString(yScale, ' ', "min")
    val yMax = getWantedString(yScale, ' ', "max")
    val xPts= breakListToSubList(token,"<xpts","</xpts>")
    val yPts= breakListToSubList(token,"<ypts","</ypts>")

    tGf.add(mapOf("xMin" to xMin))
    tGf.add(mapOf("xMax" to xMax))
    tGf.add(mapOf("yMin" to yMin))
    tGf.add(mapOf("yMax" to yMax))
    if(xPts.isNotEmpty()){
        tGf.add(mapOf("xPts" to xPts[1]))
    }else{
        tGf.add(mapOf("xPts" to "empty"))
    }
    if(yPts.isNotEmpty()){
        tGf.add(mapOf("yPts" to yPts[1]))
    }else{
        tGf.add(mapOf("yPts" to "empty"))
    }

    return tGf

}

fun gettingGroup(token: MutableList<String>,modelName: String, groupNameToken: String): MutableList<Map<String,Any>>{
    var tGroup= mutableListOf<Map<String,Any>>()
    val groupName = getWantedString(groupNameToken, ' ', "name")
    val run =  getWantedString(groupNameToken,' ',"run")
    var entityNameList = mutableListOf<String>()

    tGroup.add(mapOf("modelName" to modelName))
    tGroup.add(mapOf("groupName" to groupName))
    tGroup.add(mapOf("run" to run))

    val groupEntityList = getDataInTags(token,"<entity")


    for (index in groupEntityList.indices){
        entityNameList.add(getWantedString(groupEntityList[index],' ', "name"))
    }

    tGroup.add(mapOf("entityNameList" to entityNameList))

    return tGroup
}

fun gettingModules(tokens: MutableList<String>,modelName: String): MutableList<Map<String,Any>>{

    var connectionTo=""
    var connectionFrom=""
    var tModule= mutableListOf<Map<String,Any>>()
    var connectionToFrom= mutableListOf<Map<String,String>>()
    val moduleNameToken = getDataInTag(tokens, "<module")
    val moduleName = getWantedString(moduleNameToken, ' ', "name")
    val moduleConnectionList= getDataInTags(tokens,"<connect")

    tModule.add(mapOf("modelName" to modelName))
    tModule.add(mapOf("moduleName" to moduleName))

    for(index in moduleConnectionList.indices){
        connectionTo = getWantedString(moduleConnectionList[index], ' ', "to")
        connectionFrom = getWantedString(moduleConnectionList[index],' ', "from")
        connectionToFrom.add( mapOf(connectionTo to connectionFrom))
    }

    tModule.add(mapOf("connectionToFrom" to connectionToFrom))

    return tModule


}

fun gettingModelVariables(tokens: MutableList<MutableList<String>>,modelNameList: MutableList<String>){

    //println(tokens)
    var tModule = mutableMapOf<Int,Any>()
    var tStock = mutableMapOf<Int,Any>()
    var tFlow  = mutableMapOf<Int,Any>()
    var tAux = mutableMapOf<Int,Any>()
    var tGf = mutableMapOf<Int,Any>()
    var tGroup = mutableMapOf<Int,Any>()

    var counterModule=0
    var counterStock=0
    var counterFlow=0
    var counterAux=0
    var counterGf=0
    var counterGroup=0

    for(i in tokens.indices){
        val moduleList = separateSameTags(tokens[i], "<module", "</module>")
        for((index, tokens) in moduleList.withIndex()){
            tModule[counterModule]=gettingModules(moduleList[index],modelNameList[i])
            counterModule++
        }
    }
    println(tModule)
    println("\n")
    for(i in tokens.indices){
        val stockList = separateSameTags(tokens[i],"<stock","</stock>")
        val stockNameToken = getDataInTags(tokens[i], "<stock")
        for((index)in stockList.withIndex()){
            tStock[counterStock]=gettingStocks(stockList[index],modelNameList[i],stockNameToken[index])
            counterStock++
        }
    }
    println(tStock)
    println("\n")
    for(i in tokens.indices){
        val flowList = separateSameTags(tokens[i],"<flow","</flow>")
        val flowNameToken = getDataInTags(tokens[i], "<flow")
        for((index)in flowList.withIndex()){
            tFlow[counterFlow]=gettingFlows(flowList[index],modelNameList[i],flowNameToken[index])
            counterFlow++
        }
    }
    println(tFlow)
    println("\n")
    for(i in tokens.indices){
        val auxList = separateSameTags(tokens[i],"<aux","</aux>")
        val auxNameToken = getDataInTags(tokens[i], "<aux")
        for((index)in auxList.withIndex()){
            tAux[counterAux]=gettingAux(auxList[index],modelNameList[i],auxNameToken[index])
            counterAux++
        }
    }
    println(tAux)
    println("\n")
    for(i in tokens.indices){
        val gfList = separateSameTags(tokens[i],"<gf","</gf>")
        val gfNameToken = getDataInTags(tokens[i], "<gf")
        for((index)in gfList.withIndex()){
            tGf[counterGf]=gettingGf(gfList[index],modelNameList[i],gfNameToken[index])
            counterGf++
        }
    }
    println(tGf)
    println("\n")
    for(i in tokens.indices){
        val groupList = separateSameTags(tokens[i],"<group","</group>")
        val groupNameToken = getDataInTags(tokens[i], "<group")
        for((index)in groupList.withIndex()){
            tGroup[counterGroup]= gettingGroup(groupList[index],modelNameList[i],groupNameToken[index])
            counterGroup++
        }
    }
    println(tGroup)
    println("\n")



}

fun gettingModelTagData(tokens: MutableList<String>,rootModelName: String):Map<String, Any> {

    val modelNameList = mutableListOf<String>()
    val modelList = separateSameTags(tokens, "<model", "</model>")
    // println(modelList)


    if (modelList.isEmpty()){

        return mapOf("Error" to "Model tag is not included in XMILE dokument!")

    }

    for((index) in modelList.withIndex()){
        if (index==0){
            //println(modelList[index])
            val rootModel = getDataInTag(modelList[index], "<model")
            var nameRouteModel = getWantedString(rootModel, ' ', "name")

            if (nameRouteModel==""){

                nameRouteModel=rootModelName
                modelNameList.add(nameRouteModel)
                //println(nameRouteModel)

            }
        }else{

            val rootModel = getDataInTag(modelList[index], "<model")
            val name= getWantedString(rootModel, ' ', "name")
            if (name==""){
                return mapOf("Error" to "Model or models beyond the root model are not properly named!")
            }
            modelNameList.add(name)

        }
    }

    gettingModelVariables(modelList,modelNameList)


    return mapOf("OK" to "OK")

    /**
     * Working Base-Level Conformance: 3. MUST include at least one <model> tag (Section 2) (confirmed with test4)
     *                                 4. MUST name models beyond the root model (Section 4) (confirmed with test17)
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
    val usesSubModels = getLowerLevelOfList(tokens, "<options", "</options>")
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
        var methodSD = getWantedString(simSpecsTopString, ' ', "method")
        var timeUnitSD = getWantedString(simSpecsTopString, ' ', "time_units")
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


