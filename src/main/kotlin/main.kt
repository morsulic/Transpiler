
import hr.unipu.transpiler.ModelTemplate.getKSDTemplate
import hr.unipu.transpiler.controller.breakListToSubList
import hr.unipu.transpiler.controller.createListOfStrings
import hr.unipu.transpiler.controller.getDataInTag
import hr.unipu.transpiler.controller.getWantedString


fun main() {
    //ReadFromFile("hares_and_foxes")
    //Lexer("hares_and_foxes")

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

    /**
     * Testing functionality of Base-Level Conformance 3. MUST include at least one <model> tag (Section 2) and
     * 4. MUST name models beyond the root model (Section 4)
     */
    Lexer("Test4WithOutModel") //Testing that at least one model tag must exist in XMILE format ???


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
        var versionXMILE = getWantedString(list, ' ', "version")
        var xmlns = getWantedString(list, ' ', "xmlns")
        var xmlns1 = getWantedString(list, ' ', "xmlns:isee")

    /**
     * Printing all data needed for 1. Base-Level Conformance
     */
       //println(versionXMILE)
       //println(xmlns)
       //println(xmlns1)

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

    if(headerList.isNotEmpty()) {
        val modelName = breakListToSubList(headerList, "<name", "</name>")
        val modelVendor = breakListToSubList(headerList, "<vendor", "</vendor>")
        val productName = breakListToSubList(headerList, "<product", "</product>")
        val modelProductList = getDataInTag(headerList, "<product")
        var versionProduct = getWantedString(modelProductList, ' ', "version")

        if (modelName.isEmpty() || modelVendor.isEmpty() || productName.isEmpty() || versionProduct == "" ){

            return mapOf("Error" to "Header tag not properly configured!!!")

        }else{
            var modelNameTxt = modelName[2]
            val modelVendorTxt = modelVendor[2]
            val productNameTxt = productName[2]

            /**
             * Returning the values in header tag with data saved in map structure
             */

            val HeaderTagDataMap =mapOf("Model name" to modelNameTxt, "Vendor" to modelVendorTxt, "Product name" to productNameTxt,"Product version" to versionProduct)


            return HeaderTagDataMap
        }

    }
    /**
     * Printing all data needed for 2. Base-Level Conformance
     */
    //println(modelNameTxt)
    //println(modelVendorTxt)
    //println(productNameTxt)
    //println(versionProduct)


     return mapOf("Error" to "Header tag not properly configured!!!")

    /**
     * Working ++ (tested and confirmed rules with Test9, Test10 and Test11)
     */
}

/**
 * XMILE file Base-Level Conformance
 * 3. MUST include at least one <model> tag (Section 2)
 * 4. MUST name models beyond the root model (Section 4)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingModelTagData(tokens: MutableList<String>):Map<String, Any> {



        val modelList = breakListToSubList(tokens, "<model", "</model>")
        println(modelList)

    if (modelList.isEmpty()){

        return mapOf("Error" to "Model tag is not included in XMILE dokument!")

    }
        //var modelNameList = getDataInTag(modelList, "<model name")
        //modelNameList=modelNameList.dropLast(1)
        //val string =java.lang.String.join(separator, modelNameList)
        //println(string)
        //var modelListMultipl = separateSameTags(modelList,string,"</model>")
        //println(modelListMultipl)
        //var modelName = getWantedString(modelNameList, ' ', "name")
        //println(" ")

        //println(modelName)

    return mapOf("OK" to "OK")
}
/**
 * XMILE file Base-Level Conformance
 * 7. MUST include, when using optional features, the <options> tag with those features specified (Section 2.2.1)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingOptionsTagData(tokens: List<String>):String{
    //Options ->Default simulation specifications for this model.
    val TOP_OPTIONS = "<options"
    val BOTTOM_OPTIONS ="</options>"
    val USES_ARRAYS = "<uses_arrays/>"
    val USES_MACROS = "<uses_macros/>"


    return ""
}


/**
 * XMILE file Base-Level Conformance
 * 8. MUST contain at least one set of simulation specifications (Section 2.3)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingSimSpecsTagData(tokens: MutableList<String>): Map<String, Any> {


    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val simSpecs = breakListToSubList(tokens,"<sim_specs","</sim_specs>")
    val startOfInterval = breakListToSubList(simSpecs,"<start>","</start>")
    val endOfInterval = breakListToSubList(simSpecs,"<stop>","</stop>")
    val interval = breakListToSubList(simSpecs,"<dt>","</dt>")

    val simSpecsTopString = getDataInTag(tokens,"<sim_specs")


    var methodSD =getWantedString(simSpecsTopString,' ',"method")
    var timeUnitSD =getWantedString(simSpecsTopString,' ',"time_units")


    var initialTime = startOfInterval[2].toFloat()
    var finalTime = endOfInterval[2].toFloat()
    var timeStep = interval[2].toFloat()


    /**
     * Printing all data needed for 8. Base-Level Conformance
     */
    //println(methodSD)
    //println(timeUnitSD)
    //println(initialTime)
    //println(finalTime)
    //println(timeStep)

    /**
     * Returning the values in header tag with data saved in map structure
     */

    val SimSpecsTagDataMap =mapOf("Method" to methodSD, "Time unit" to timeUnitSD, "Initial time" to initialTime,
        "Final time" to finalTime, "Time step" to timeStep)


    return SimSpecsTagDataMap

    /**
     * Working ++
     * But needs improvement for checking errors in simSpecs tag and resolving inconsistencies between multiple sim_specs
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

    val TOP_VIEW = "<views"
    val BOTTOM_VIEW ="</views>"
    tokens = removeWantedTagBlock(tokens,TOP_VIEW,BOTTOM_VIEW)
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
    //println(headerMap)

    //3. Getting data from model of hr.unipu.transpiler.XMILE format
    val model = gettingModelTagData(tokens)
    println(model)

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val simSpecsMap = gettingSimSpecsTagData(tokens)
    //println(simSpecsMap)

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    //val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)

}




