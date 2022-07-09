import hr.unipu.transpiler.ModelTemplate.getKSDTemplate
import hr.unipu.transpiler.controller.*
import hr.unipu.transpiler.model.TranspilerModule
import hr.unipu.transpiler.model.TranspilerStock


fun main() {
    //ReadFromFile("hares_and_foxes")
    Lexer("hares_and_foxes")

    /**
     * Testing functionality of removing view tag
     * Tests 1 to 3
     */

    //Lexer("Test1RemoveView")// Main test for remove view +
    //Lexer("Test2RemoveViewOneMore") //Testing removing one more view block +
    //Lexer("Test3RemoveViewErrorTags") //Testing remove view with one view block without end tag

    /**
     * Testing functionality of Base-Level Conformance 1. MUST include an <xmile> tag that contains both the version of
     * XMILE used and the XMILE XML namespace (Section 2)
     */
    //Lexer("Test6WithOutXMILETag") //Testing  XMILE tag missing
    //Lexer("Test7WithOutXMILETag") //Testing  XMILE tag version missing
    //Lexer("Test8WithOutXMILETag") //Testing  XMILE tag namespace missing

    /**
     * Testing functionality of Base-Level Conformance  2. MUST include a <header> tag (Section 2) with sub-tags
     * <vendor> and <product> with its version number (Section 2.2)
     */
    //Lexer("Test9WithOutHeaderTag") //Testing header tag missing
    //Lexer("Test10WithOutHeaderTag") //Testing header tag name missing
    //Lexer("Test11WithOutHeaderTag") //Testing header tag product version missing
    //Lexer("Test16WithOutHeaderTag") //Testing header tag product version value missing
    /**
     * Testing functionality of Base-Level Conformance 3. MUST include at least one <model> tag (Section 2) and
     * 4. MUST name models beyond the root model (Section 4)
     */
    //Lexer("Test4WithOutModel") //Testing that at least one model tag must exist in XMILE format
    //Lexer("Test17WithOutSubModelName") //Testing that all submodels are properly named

    /**
     * Testing functionality of Base-Level Conformance 7. MUST include, when using optional features, the <options>
     * tag with those features specified (Section 2.2.1)
     */
    //Lexer("Test15OptionsUsesSubModels")


    /**
     * Testing functionality of Base-Level Conformance  8. MUST contain at least one set of simulation
     * specifications (Section 2.3)
     */
    //Lexer("Test12WithOutSimSpecs") //Testing that at least one SimSpecss tag must exist in XMILE format
    //Lexer("Test13WithOutSimSpecs") //Testing sim_specs start of interval tag value missing
    //Lexer("Test14WithOutSimSpecs") //Testing sim_specs dt tag value missing used default value time step = 1.0

    /**
     * Testing functionality of Base-Level Conformance 9. MUST support model behaviors (Section 2.6)
     */
    //Lexer("Test5BehaviorTag") //Testing getting data from behaviour tag

    /**
     * Creating test file
     */
    val KSDmodel= getKSDTemplate() //uncomment the import
    //CreateFile("proba1", KSDmodel) //uncomment the import
}

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
fun gettingModules(tokens: MutableList<String>): TranspilerModule{

    var connectionTo = ""
    var connectionFrom = ""

    val moduleNameToken = getDataInTag(tokens, "<module")
    val moduleName = getWantedString(moduleNameToken, ' ', "name")
    val moduleConnectionList= getDataInTags(tokens,"<connect")

    val tModule = TranspilerModule()
    tModule.moduleName=moduleName

    for(index in moduleConnectionList.indices){
        connectionTo = getWantedString(moduleConnectionList[index], ' ', "to")
        connectionFrom = getWantedString(moduleConnectionList[index],' ', "from")
        tModule.connectionToFrom.add( mapOf(connectionTo to connectionFrom))
    }

    //println(tModule.moduleName + " " + tModule.connectionToFrom)
    return tModule
}

fun gettingStocks(tokens: MutableList<String>): TranspilerStock {
    println(tokens)
    val stockNameToken = getDataInTag(tokens, "<stock")
    val stockName = getWantedString(stockNameToken, ' ', "name")
    val valueEquationToken = breakListToSubList(tokens, "<eqn", "</eqn>")
    val inflowsList = separateSameTags(tokens,"<inflow", "</inflow>")
    val outflowsList = separateSameTags(tokens, "<outflow", "</outflow>")
    println(inflowsList)
    println(outflowsList)
    val inflows = mutableListOf<String>()
    val outflows = mutableListOf<String>()

    for(index in inflowsList.indices){
        inflows.add(inflowsList[index][index])
    }
    for(index in outflowsList.indices){
        outflows.add(outflowsList[index][index])
    }


    val nonNegative = false



    val tStock = TranspilerStock()

    tStock.stockName = stockName
    tStock.initialValueEquation = valueEquationToken[1]
    tStock.inflows=inflows
    tStock.outflows=outflows

    println(tStock.stockName+" "+tStock.inflows+" "+tStock.outflows+" "+tStock.initialValueEquation)
    return tStock
}
fun gettingModelVariables(tokens: MutableList<MutableList<String>>): Map<String, Any>{

    val moduleList = separateSameTags(tokens[0], "<module", "</module>")
    for((index) in moduleList.withIndex()){
    val tModule = gettingModules(moduleList[index])}
    val tStocks = gettingStocks(tokens[1])
    return(mapOf("OK" to "OK"))
}
fun gettingModelTagData(tokens: MutableList<String>, modelName: String):Map<String, Any> {

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

                    nameRouteModel=modelName
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
    var list = gettingModelVariables(modelList)

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


/**
 * XMILE file
 * 11. MUST support all base functionality objects (Section 3.1 and all subsections)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

/**
 * Creating tokens and removing wanted values with lexer function
 */


fun Lexer(name: String){


    /**
     * Generating wanted token blocks by calling functions:
     *                                              createListOfStrings
     *                                              removeViewFromText
     */

    //Creating list of strings (token blocks) from hr.unipu.transpiler.XMILE format
    var tokens = createListOfStrings(name)

    //Removing view tags if possible
    tokens = removeWantedTagBlock(tokens,"<views","</views>")
    tokens.remove("<model_units>")
    tokens.remove("<model_units/>")
    //println(tokens)


    /**
     * Getting wanted values form generated token blocks with functions:
     *                                                 gettingXMILETagData
     *                                                 gettingHeaderData
     *                                                 gettingModelTagData
     */
    println(" ")
    //1. Getting data from XMILE tag of hr.unipu.transpiler.XMILE format
    val XMILETopMap =gettingXMILETagData(tokens)
    //println(XMILETopMap)

    //2. Getting data from header of hr.unipu.transpiler.XMILE format
    val headerMap = gettingHeaderTagData(tokens)
    val modelName = headerMap.getValue("Model name").toString()
    //println(headerMap)

    //3. Getting data from model of hr.unipu.transpiler.XMILE format
    val model = gettingModelTagData(tokens,modelName)
    println(model)

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val simSpecsMap = gettingSimSpecsTagData(tokens)
    //println(simSpecsMap)

    //Getting data from options of hr.unipu.transpiler.XMILE format
    val options = gettingOptionsTagData(tokens)
    //println(options)

    //Getting data from behaviour of hr.unipu.transpiler.XMILE format
    val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)



}




