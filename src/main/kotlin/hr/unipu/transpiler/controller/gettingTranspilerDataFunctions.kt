package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.transpilerDataMap

/**
 * XMILE file Base-Level Conformance
 * 1. MUST include an <xmile> tag that contains both the version of XMILE used and the XMILE XML namespace (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingXMILETagData(tokens: MutableList<String>) {

    val list = getDataInTag(tokens, "<xmile")
    var versionXMILE = getWantedString(list, "version")
    var xmlns = getWantedStringXMILETag(list, "xmlns")
    var xmlns1 = getWantedStringXMILETag(list, "xmlns:isee")

    if (versionXMILE == "" || xmlns == "") {
        error(
            "Error: XMILE tag not properly configured!!! 1. MUST include an <xmile> tag that contains both the " +
                    "version of XMILE used and the XMILE XML namespace (Section 2)"
        )
    } else {
        transpilerDataMap += mapOf("Version" to versionXMILE)
        transpilerDataMap += mapOf("xmlns1" to xmlns)
        transpilerDataMap += mapOf("xmlnsISEE" to xmlns1)
    }
    /*
     * Working ++ (tested and confirmed rules with Test6, Test7 and Test8)
     */
}

/**
 * XMILE file Base-Level Conformance
 * 2. MUST include a <header> tag (Section 2) with sub-tags <vendor> and <product> with its version number (Section 2.2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
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

/*fun gettingHeaderTagData(tokens: MutableList<String>) {

    val headerList = breakListToSubList(tokens, "<header", "</header>")

    if (checkingTagListIsAdded(headerList, "Header tag")) {
        val modelName = breakListToSubList(headerList, "<name", "</name>")
        val modelVendor = breakListToSubList(headerList, "<vendor", "</vendor>")
        val productName = breakListToSubList(headerList, "<product", "</product>")
        val modelProductList = getDataInTag(headerList, "<product")
        var versionProduct = getWantedString(modelProductList, "version")
        var subModelOptionsBool = gettingOptionsTagData(headerList)

        if (modelName.isEmpty() || modelVendor.isEmpty() || productName.isEmpty() || versionProduct == "") {

            error(
                "Error: Header tag not properly configured!!! 2. MUST include a <header> tag (Section 2) with " +
                        "sub-tags <vendor> and <product> with its version number (Section 2.2)"
            )

        } else {
            var modelNameTxt = modelName[1]
            val modelVendorTxt = modelVendor[1]
            val productNameTxt = productName[1]

            transpilerDataMap += mapOf("Model name" to modelNameTxt)
            transpilerDataMap += mapOf("Vendor" to modelVendorTxt)
            transpilerDataMap += mapOf("Product name" to productNameTxt)
            transpilerDataMap += mapOf("Product version" to versionProduct)
            transpilerDataMap += mapOf("Options SubModel" to subModelOptionsBool.toString())

        }
    }
    /*
     * Working ++ (tested and confirmed rules with Test9, Test10 and Test11)
     */


}*/

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
 *             gettingGr  (Section 3.1.4)****
 *             gettingGroup (Section 3.1.5)****
 *             gettingModules (Section 3.1.5)
 */

fun gettingStocks(tokensList: MutableList<String>, modelName: String, stockNameToken: String) {

    var stockName = getWantedString(stockNameToken, "name")
    val stockNameHelper =stockName
    stockName=stockName.lowercase().replaceFirstChar { it.uppercase() }

    if (checkingTagIsAdded(stockName, "Stock name")) {

        val valueEquationToken = breakListToSubList(tokensList, "<eqn", "</eqn>")
        val inflowsList = separateSameTags(tokensList, "<inflow", "</inflow>")
        val outflowsList = separateSameTags(tokensList, "<outflow", "</outflow>")
        val unit = breakListToSubList(tokensList, "<units", "</units>")
        val description = breakListToSubList(tokensList, "<doc", "</doc>")
        val nonNegative = preparingTagNonNegativeStatus(tokensList)
        var inflows = preparingInflowsAndOutflowsOfStock(inflowsList, "inflows")
        var outflows = preparingInflowsAndOutflowsOfStock(outflowsList, "outflows")

        transpilerDataMap += mapOf("$modelName StockName: $stockName" to "$stockName")

        if (checkingTagIsAdded(valueEquationToken[1], "Stock eqn")) {

            preparingEquationsInConstantsOrConverters(modelName, stockNameHelper,"Stock", valueEquationToken[1])
            transpilerDataMap += mapOf("$modelName StockEquationTokenValue: $stockName" to "${valueEquationToken[1]}")
            transpilerDataMap += mapOf("$modelName StockInflowOutflowValue: $stockName" to "$inflows$outflows")
            transpilerDataMap += mapOf("$modelName StockNonNegativeValueOf: $stockName" to "${nonNegative.toString()}")

            if (unit.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName StockUnitValue: $stockName" to "${unit[1]}")

            if (description.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName StockDescriptionValue: $stockName" to "${description[1]}")

        }
    }
}

fun gettingFlows(tokensList: MutableList<String>, modelName: String, flowNameToken: String) {

    var flowName = getWantedString(flowNameToken, "name").lowercase()
    val flowNameHelper = flowName
    flowName = flowName.lowercase()

    if (checkingTagIsAdded(flowName, "Flow name")) {

        val valueEquationToken = breakListToSubList(tokensList, "<eqn", "</eqn>")
        val unit = breakListToSubList(tokensList, "<units", "</units>")
        val description = breakListToSubList(tokensList, "<doc", "</doc>")
        val nonNegative = preparingTagNonNegativeStatus(tokensList)

        transpilerDataMap += mapOf("$modelName FlowName: $flowName" to "$flowName")

        if (checkingTagIsAdded(valueEquationToken[1], "Flow eqn")) {

            transpilerDataMap += mapOf("$modelName FlowNonNegativeValueOf: $flowName" to "${nonNegative.toString()}")
            preparingEquationsInConstantsOrConverters(modelName, flowNameHelper,"Flow", valueEquationToken[1])

            if (unit.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName FlowUnitValue: $flowName" to "${unit[1]}")

            if (description.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName FlowDescriptionValue: $flowName" to "${description[1]}")
        }
    }
}

fun gettingAux(tokensList: MutableList<String>, modelName: String, auxNameToken: String) {

    var auxName = getWantedString(auxNameToken, "name")
    val auxNameHelper = auxName
    auxName=auxName.lowercase()

    if (checkingTagIsAdded(auxName, "Aux name")) {

        val valueEquationToken = breakListToSubList(tokensList, "<eqn", "</eqn>")
        val unit = breakListToSubList(tokensList, "<units", "</units>")
        val description = breakListToSubList(tokensList, "<doc", "</doc>")
        val nonNegative = preparingTagNonNegativeStatus(tokensList)

        transpilerDataMap += mapOf("$modelName AuxName: $auxName" to "$auxName")

        if (checkingTagIsAdded(valueEquationToken[1], "Aux eqn")) {

            transpilerDataMap += mapOf("$modelName AuxNonNegativeValueOf: $auxName" to "${nonNegative.toString()}")
            preparingEquationsInConstantsOrConverters(modelName, auxNameHelper, "Aux", valueEquationToken[1])

            if (unit.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName AuxUnitValue: $auxName" to "${unit[1]}")

            if (description.isNotEmpty()) transpilerDataMap +=
                mapOf("$modelName AuxDescriptionValue: $auxName" to "${description[1]}")
        }
    }
}

fun gettingModules(tokensList: MutableList<String>, modelName: String, moduleNameToken: String) {

    var moduleName = getWantedString(moduleNameToken, "name").lowercase().replaceFirstChar { it.uppercase() }

    if (checkingTagIsAdded(moduleName, "Module name")) {

        val description = breakListToSubList(tokensList, "<doc", "</doc>")
        val moduleConnectionList = getDataInTags(tokensList, "<connect")
        var connectionTo = ""
        var connectionFrom = ""

        transpilerDataMap += mapOf("$modelName ModuleName: $moduleName" to "$moduleName")
        if (description.isNotEmpty()) {
            transpilerDataMap += mapOf("$modelName ModuleDescriptionValue: $moduleName" to "${description[1]}")
        }
        for (index in moduleConnectionList.indices) {
            connectionTo += getWantedString(moduleConnectionList[index], "to") + ","
            connectionFrom += getWantedString(moduleConnectionList[index], "from") + ","
        }
        transpilerDataMap += mapOf("$modelName ModuleConnectionsTo: $moduleName" to "$connectionTo")
        transpilerDataMap += mapOf("$modelName ModuleConnectionsFrom: $moduleName" to "$connectionFrom")

    }
}

fun gettingModelsVariables(tokens: MutableList<MutableList<String>>, modelNameList: MutableList<String>) {

    for (i in tokens.indices) {
        val stockList = separateSameTags(tokens[i], "<stock", "</stock>")
        val stockNameToken = getDataInTags(tokens[i], "<stock")
        for ((index) in stockList.withIndex()) {
            gettingStocks(stockList[index], modelNameList[i], stockNameToken[index])

        }
    }

    for (i in tokens.indices) {
        val flowList = separateSameTags(tokens[i], "<flow", "</flow>")
        val flowNameToken = getDataInTags(tokens[i], "<flow")
        for ((index) in flowList.withIndex()) {
            gettingFlows(flowList[index], modelNameList[i], flowNameToken[index])
        }
    }

    for (i in tokens.indices) {
        val auxList = separateSameTags(tokens[i], "<aux", "</aux>")
        val auxNameToken = getDataInTags(tokens[i], "<aux")
        for ((index) in auxList.withIndex()) {
            gettingAux(auxList[index], modelNameList[i], auxNameToken[index])

        }
    }

    for (i in tokens.indices) {
        val moduleList = separateSameTags(tokens[i], "<module", "</module>")
        val moduleNameToken = getDataInTags(tokens[i], "<module")
        for ((index) in moduleList.withIndex()) {
            gettingModules(moduleList[index], modelNameList[i], moduleNameToken[index])
        }
    }

    /*for (i in tokens.indices) {
           val gfList = separateSameTags(tokens[i], "<gf", "</gf>")
           val gfNameToken = getDataInTags(tokens[i], "<gf")
           for ((index) in gfList.withIndex()) {
               gettingGf(gfList[index], tModel, gfNameToken[index])
           }
       }*/

    /*for (i in tokens.indices) {
        val groupList = separateSameTags(tokens[i], "<group", "</group>")
        val groupNameToken = getDataInTags(tokens[i], "<group")
        for ((index) in groupList.withIndex()) {
            gettingGroup(groupList[index], tModel, groupNameToken[index])
        }
    }*/
}

fun gettingModelTagData(tokens: MutableList<String>, rootModelName: String) {

    val modelNameList = mutableListOf<String>()
    val modelList = separateSameTags(tokens, "<model", "</model>")

    if (modelList.isEmpty()) {

        error("Error: Model tag is not included in XMILE dokument!")

    }

    for ((index) in modelList.withIndex()) {
        if (index == 0) {
            val rootModel = getDataInTag(modelList[index], "<model")
            var nameRouteModel = getWantedString(rootModel, "name")

            if (nameRouteModel == "") {

                nameRouteModel = rootModelName
                modelNameList.add(nameRouteModel)

            }
        } else {

            val rootModel = getDataInTag(modelList[index], "<model")
            val name = getWantedString(rootModel, "name")
            if (name == "") {
                error("Error: Model or models beyond the root model are not properly named!")
            }
            modelNameList.add(name)

        }
    }

    gettingModelsVariables(modelList, modelNameList)
    for (i in modelNameList.indices) {
        gettingSimSpecsTagData(modelList[i], modelNameList[i])
    }
    transpilerDataMap += mapOf("ModelNamesListInStringForm" to "" + modelNameList.toString())

    /**
     * Working Base-Level Conformance: 3. MUST include at least one <model> tag (Section 2) (confirmed with test4)
     *                                 4. MUST name models beyond the root model (Section 4) (confirmed with test17)
     *                                 11.  MUST support all base functionality objects (Section 3.1 and all subsections)
     */
}


/**
 * XMILE file Base-Level Conformance
 * 8. MUST contain at least one set of simulation specifications (Section 2.3)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingSimSpecsTagData(tokens: MutableList<String>, modelName: String = "default") {

    val simSpecsList = breakListToSubList(tokens, "<sim_specs", "</sim_specs>")

    if (simSpecsList.isNotEmpty()) {
        val startOfInterval = breakListToSubList(simSpecsList, "<start>", "</start>")
        val endOfInterval = breakListToSubList(simSpecsList, "<stop>", "</stop>")
        var interval = breakListToSubList(simSpecsList, "<dt>", "</dt>")
        val simSpecsTopString = getDataInTag(tokens, "<sim_specs")
        var methodSD = getWantedString(simSpecsTopString, "method")
        var timeUnitSD = getWantedString(simSpecsTopString, "time_units")
        val a = startOfInterval[1].toDoubleOrNull()
        val b = endOfInterval[1].toDoubleOrNull()
        val c = interval[1].toDoubleOrNull()

        if ((startOfInterval.isEmpty() || endOfInterval.isEmpty() || a == null || b == null) && modelName == "default") {

            error(
                "Error: Sim_specs start or stop tag not properly configured!!! 8. MUST contain at least one set of " +
                        "simulation specifications (Section 2.3)"
            )

        } else if (c == null || interval.isEmpty() && (a != null && b != null)) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = 1.0

            methodSD = preparingTypeOfSimSpecsIntegration(methodSD)

            transpilerDataMap += "$modelName SimSpecs Method:" to "$methodSD"
            transpilerDataMap += "$modelName SimSpecs Time unit:" to "$timeUnitSD"
            transpilerDataMap += "$modelName SimSpecs Initial time:" to "$initialTime"
            transpilerDataMap += "$modelName SimSpecs Final time:" to "$finalTime"
            transpilerDataMap += "$modelName SimSpecs Time step:" to "$timeStep"


        } else if (a != null && b != null && c != null) {

            var initialTime = startOfInterval[1].toDouble()
            var finalTime = endOfInterval[1].toDouble()
            var timeStep = interval[1].toDouble()

            methodSD = preparingTypeOfSimSpecsIntegration(methodSD)

            transpilerDataMap += "$modelName SimSpecs Method:" to "$methodSD"
            transpilerDataMap += "$modelName SimSpecs Time unit:" to "$timeUnitSD"
            transpilerDataMap += "$modelName SimSpecs Initial time:" to "$initialTime"
            transpilerDataMap += "$modelName SimSpecs Final time:" to "$finalTime"
            transpilerDataMap += "$modelName SimSpecs Time step:" to "$timeStep"


        } else if (modelName == "default") {
            error(
                "Error: Sim_specs tag not properly configured!!! 8. MUST contain at least one set of simulation " +
                        "specifications (Section 2.3)"
            )
        }
    } else if (modelName == "default") {
        error("Error: Sim_specs tag is not included in XMILE dokument!")
    } else {
        transpilerDataMap += mutableMapOf("$modelName SimSpecs empty:" to modelName)
    }
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

fun gettingBehaviorTagData(tokens: MutableList<String>): String {

    //Getting data from behavior of hr.unipu.transpiler.XMILE format
    val behavior = breakListToSubList(tokens, "<behavior", "</behavior>")
    //println(behavior)

    var list = ""
    return list
}


