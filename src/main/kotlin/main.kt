
import hr.unipu.transpiler.FunctionsWithFiles.CreateFile
import hr.unipu.transpiler.ModelTemplate.getKSDTemplate
import hr.unipu.transpiler.controller.*


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
     * Testing functionality of Base-Level Conformance 3. MUST include at least one <model> tag (Section 2)
     */
    //Lexer("Test4WithOutModel") //Testing that at least one model tag must exist in XMILE format +

    /**
     * Creating test file
     */
    val KSDmodel= getKSDTemplate() //uncomment the import
    CreateFile("proba1", KSDmodel) //uncomment the import
}

/**
 * XMILE file Base-Level Conformance
 * 1. MUST include an <xmile> tag that contains both the version of XMILE used and the XMILE XML namespace (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingXMILETagData(tokens: List<String>): Map<String, Any> {
    //hr.unipu.transpiler.XMILE tokens (tags) only version  ( version 1.0)
    val TOP_XMILE ="<xmile"

    val list =getDataInTag(tokens,TOP_XMILE)
    var versionXMILE =getWantedString(list,' ',"version")
    var xmlns = getWantedString(list,' ',"xmlns")
    var xmlns1 = getWantedString(list,' ',"xmlns:isee")

    /**
     * Printing all data needed for 1. Base-Level Conformance
     */
       //println(versionXMILE)
       //println(xmlns)
       //println(xmlns1)

    /**
     * Returning the values in XMILE tag with data saved in map structure
     */

    val XMILETagDataMap =mapOf("Version" to versionXMILE, "xmlns" to xmlns, "xmlnsISEE" to xmlns1)

     /**
     * Working ++
     */

    return XMILETagDataMap
}

/**
 * XMILE file Base-Level Conformance
 * 2. MUST include a <header> tag (Section 2) with sub-tags <vendor> and <product> with its version number (Section 2.2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingHeaderTagData(tokens: MutableList<String>): Map<String, Any> {
    //Header tokens
    val TOP_HEADER ="<header>"
    val BOTTOM_HEADER ="</header>"
    val H_TOP_NAME ="<name>"
    val H_BOT_NAME ="</name>"
    val H_TOP_VENDOR="<vendor>"
    val H_BOT_VENDOR="</vendor>"
    val H_TOP_PRODUCT="<product"


    val headerList=breakListToSubList(tokens,TOP_HEADER,BOTTOM_HEADER)

    //println(headerList)

    val modelName = breakListToSubList(headerList, H_TOP_NAME, H_BOT_NAME)
    var modelNameTxt = modelName[2]

    val modelVendor = breakListToSubList(headerList, H_TOP_VENDOR, H_BOT_VENDOR)
    val modelVendorTxt = modelVendor[2]

    val modelProductList = getDataInTag(headerList, H_TOP_PRODUCT)
    var versionProduct = getWantedString(modelProductList, ' ', "version")

    /**
     * Printing all data needed for 2. Base-Level Conformance
     */
    //println(modelNameTxt)
    //println(modelVendorTxt)
    //println(versionProduct)

    /**
     * Returning the values in header tag with data saved in map structure
     */

    val HeaderTagDataMap =mapOf("Name" to modelNameTxt, "Vendor" to modelVendorTxt, "Product version" to versionProduct)
    /**
     * Working ++
     */

    return HeaderTagDataMap
}

/**
 * XMILE file Base-Level Conformance
 * 3. MUST include at least one <model> tag (Section 2)
 * 4. MUST name models beyond the root model (Section 4)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingModelTagData(tokens: MutableList<String>):String{

    /**
     * Checking if there is at least one model tag
     */
    val countOfModelTags = countTags(tokens, "<model", "</model>")

    if(countOfModelTags>0) {
        val modelList = breakListToSubList(tokens, "<model", "</model>")
        println(modelList)
        val modelNameList = getDataInTag(modelList, "<model name")
        println(modelNameList)
        var modelName = getWantedString(modelNameList, ' ', "name")
        println(" ")
        println(countOfModelTags)
        println(modelName)
    }
    return ""
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

fun gettingSimSpecsTagData(tokens: MutableList<String>):String{
    //Simple specs ->Default simulation specifications for this model.
    val TOP_SIM_SPECS = "<sim_specs"
    val BOTTOM_SIM_SPECS ="</sim_specs>"
    val METHOD ="method"
    val TOP_START ="<start>"
    val BOT_START ="</start>"
    val TOP_STOP="<stop>"
    val BOT_STOP="</stop>"
    val TOP_DT ="<dt>"
    val BOT_DT ="</dt>"
    val NAME2="name="
    val NAME3="name>"

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val simSpecs = breakListToSubList(tokens,TOP_SIM_SPECS,BOTTOM_SIM_SPECS)
    /**
    val startOfInterval = breakListToSubList(simSpecs,TOP_START,BOT_START)
    val endOfInterval = breakListToSubList(simSpecs,TOP_STOP,BOT_STOP)
    val interval = breakListToSubList(simSpecs,TOP_DT,BOT_DT)
    val simSpecsTopString = getDataInTag(tokens,TOP_SIM_SPECS)

    var methodSD =getWantedString(simSpecsTopString,' ',"method")

    var timeUnitSD =getWantedString(simSpecsTopString,' ',"time_units")

    var initialTime = startOfInterval[0].toFloat()

    var finalTime = endOfInterval[0].toFloat()

    var timeStep = interval[0].toFloat()
*/
    /**
     * println(methodSD)
     * println(timeUnitSD)
     * println(initialTime)
     * println(finalTime)
     * println(timeStep)
     */

    return ""
}

/**
 * XMILE file
 * 9. MUST support model behaviors (Section 2.6)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */
fun gettingBehaviorTagData(tokens: List<String>):String{
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

    //1. Getting data from hr.unipu.transpiler.XMILE tag of hr.unipu.transpiler.XMILE format
    val XMILETopMap =gettingXMILETagData(tokens)
    //println(XMILETopMap)

    //2. Getting data from header of hr.unipu.transpiler.XMILE format
    val headerMap = gettingHeaderTagData(tokens)
    //println(headerMap)

    //3. Getting data from model of hr.unipu.transpiler.XMILE format
    val model = gettingModelTagData(tokens)
    //println(model)

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    //val sim_specs = gettingSimSpecsTagData(tokens)
    //println(sim_specs)

    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)

}




