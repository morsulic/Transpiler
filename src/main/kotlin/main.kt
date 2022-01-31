import java.io.FileReader
import java.io.FileWriter
var descriptionPackage="/**\n" +
        " * SD model of Simple Compound Interest.\n" +
        " *\n" +
        " * @author [Siniša Sovilj](mailto:sinisa.sovilj@unipu.hr)\n" +
        " */\n" +
        "\n" +
        "package Kotlin_models"

var constantValues="\n" +
        "open class ModelGenericSD : Model() {\n" +
        "\n" +
        "    // Static properties:\n" +
        "    companion object {\n" +
        "        const val CONSTANT_KEY = \"CONSTANT\"\n" +
        "        const val CONVERTER_KEY = \"converter\"\n" +
        "        const val INITIAL_STOCK_KEY = \"INITIAL_STOCK\"\n" +
        "        const val INFLOW_KEY = \"inflow\"\n" +
        "        const val OUTFLOW_KEY = \"outflow\"\n" +
        "        const val STOCK_KEY = \"Stock\"\n" +
        "\n" +
        "        const val CONSTANT_VALUE = 10          // [%]\n" +
        "        const val INITIAL_STOCK_VALUE = 100    // [€]\n" +
        "        const val INITIAL_TIME_VALUE = 0       // [month]\n" +
        "        const val FINAL_TIME_VALUE = 120       // [month]\n" +
        "        const val TIME_STEP_VALUE = 0.25       // [month]\n" +
        "    }"
var initializationOfModel="init {\n" +
        "\n" +
        "        // 1. Create the model (with setup of: time boundaries & time step & integrationType type)\n" +
        "        val model = this   // inheritance: Model()\n" +
        "\n" +
        "        /*\n" +
        "        val model = Model(\n" +
        "            initialTime = 0,\n" +
        "            finalTime = 100,\n" +
        "            timeStep = 0.25,\n" +
        "            integrationType = EulerIntegration()\n" +
        "        )\n" +
        "        */\n" +
        "\n" +
        "\n" +
        "        /*\n" +
        "        val model = Model(0, 100, 0.25, EulerIntegration())\n" +
        "            // alternative: Model(INITIAL_TIME_VALUE, FINAL_TIME_VALUE, TIME_STEP_VALUE, EulerIntegration())\n" +
        "        */\n" +
        "\n" +
        "\n" +
        "        // override default model properties:\n" +
        "        model.initialTime = INITIAL_TIME_VALUE\n" +
        "        model.finalTime = FINAL_TIME_VALUE\n" +
        "        model.timeStep = TIME_STEP_VALUE\n" +
        "        model.integrationType = EulerIntegration()\n" +
        "        model.name = \"Generic SD Model\"   // name is optional\n" +
        "        model.timeUnit = \"month\"          // unit is optional\n" +
        "\n" +
        "\n" +
        "\n" +
        "        // 2. Create all system elements:\n" +
        "\n" +
        "        // - 2a. Variables (Constants)\n" +
        "        val CONSTANT = model.constant(CONSTANT_KEY)\n" +
        "        val INITIAL_STOCK = model.constant(INITIAL_STOCK_KEY)\n" +
        "\n" +
        "        // - 2b. Variables (Converters)\n" +
        "        val converter = model.converter(CONVERTER_KEY)\n" +
        "\n" +
        "        // - 2c. Stocks\n" +
        "        val Stock = model.stock(STOCK_KEY)\n" +
        "\n" +
        "        // - 2d. Flows\n" +
        "        val inflow = model.flow(INFLOW_KEY)\n" +
        "        val outflow = model.flow(OUTFLOW_KEY)\n" +
        "\n" +
        "        // - 2e. Modules\n" +
        "        val Module = model.createModule(\n" +
        "            \"Module\",\n" +
        "            \"hr.unipu.ksdtoolkit.modules.ModuleGenericCompoundDecrease\"\n" +
        "        ) as ModuleGenericCompoundDecrease\n" +
        "\n" +
        "\n" +
        "        // - 2f. (Optional): Entities' descriptions\n" +
        "        CONSTANT.description = \"Annual flow rate in [%/year]\"\n" +
        "        INITIAL_STOCK.description = \"Initial capital in [EUR] in the beginning of the simulation.\"\n" +
        "        converter.description = \"Converts percentage to decimal.\"\n" +
        "        Stock.description = \"Accumulated capital in [EUR] at specific point in time.\"\n" +
        "        inflow.description = \"Interest inflow in [EUR / chosen unit of time], e.g. [EUR/month].\"\n" +
        "        outflow.description = \"Interest outflow in [EUR / chosen unit of time], e.g. [EUR/month].\"\n" +
        "\n" +
        "        // - 2g. (Optional): Entities' units\n" +
        "        CONSTANT.unit = \"%/year\"\n" +
        "        INITIAL_STOCK.unit = \"€\"\n" +
        "        Stock.unit = \"€\"\n" +
        "        inflow.unit = \"€/month\"\n" +
        "        outflow.unit = \"€/month\"\n" +
        "\n" +
        "\n" +
        "        // 3. Initial values:\n" +
        "\n" +
        "        // - 3a. Stocks\n" +
        "        Stock.initialValue = { INITIAL_STOCK }      // Accepts: Double, Int or ModelEntity\n" +
        "\n" +
        "\n" +
        "\n" +
        "        // 4. Equations:\n" +
        "\n" +
        "        // - 4a. Constants\n" +
        "        CONSTANT.equation = { CONSTANT_VALUE }\n" +
        "        INITIAL_STOCK.equation = { INITIAL_STOCK_VALUE }\n" +
        "\n" +
        "        // - 4b. Converters\n" +
        "        converter.equation = { CONSTANT / 100.0 / 12.0 }\n" +
        "\n" +
        "        // - 4c. Stocks\n" +
        "        Stock.equation = { inflow - outflow }     // Function type can be either Double or ModelEntity.\n" +
        "\n" +
        "        // - 4d. Flows:\n" +
        "        inflow.equation = { Stock * converter }    // Simplified converters so that only equations are used.\n" +
        "        outflow.equation = { Module.outflow }      // Alternative, instead of lambda, member reference: Module::outflow\n" +
        "\n" +
        "        // - 4e. Modules:\n" +
        "        Module.inflow.equation = { outflow }\n" +
        "\n" +
        "\n" +
        "    }\n" +
        "\n" +
        "}"
var a = descriptionPackage + constantValues + initializationOfModel

fun main() {
   //ReadFromFile("hares_and_foxes")
   //Lexer("hares_and_foxes")

    /**
     * Testing functionality of removing view tag
     */

    //Lexer("Test1RemoveView")// Main test for remove view  +
    //Lexer("Test2RemoveViewOneMore") //Testing removing one more view block
    //Lexer("Test3RemoveViewErrorTags") //Testing remove view with one view block without end tag

    //CreateFile("proba",a)
}

/**
 * XMILE file Base-Level Conformance
 * 1. MUST include an <xmile> tag that contains both the version of XMILE used and the XMILE XML namespace (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingXMILETagData(tokens: List<String>): String {
    //XMILE tokens (tags) only version  ( version 1.0)
    val TOP_XMILE ="<xmile"
    val BOTTOM_XMILE="</xmile>"

    val list =gettingDataInTag(tokens,TOP_XMILE)
    var versionXMILE =getWantedString(list,' ',"version")
    var xmlns = getWantedString(list,' ',"xmlns")
    var xmlns1 = getWantedString(list,' ',"xmlns:isee")
    /**
     * println(versionXMILE)
     * println(xmlns)
     * println(xmlns1)
     */

    return list
}

/**
 * XMILE file Base-Level Conformance
 * 2. MUST include a <header> tag (Section 2) with sub-tags <vendor> and <product> with its version number (Section 2.2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingHeaderTagData(tokens: List<String>):String{
    //Header tokens
    val TOP_HEADER ="<header>"
    val BOTTOM_HEADER ="</header>"
    val H_TOP_NAME ="<name>"
    val H_BOT_NAME ="</name>"
    val H_TOP_VENDOR="<vendor>"
    val H_BOT_VENDOR="</vendor>"
    val H_TOP_PRODUCT="<product"


    val list =""
    val headerList=breakingListToSubList(tokens,TOP_HEADER,BOTTOM_HEADER)

    val modelName=breakingListToSubList(headerList,H_TOP_NAME,H_BOT_NAME)
    var modelNameTxt= modelName[0]

    val modelVendor=breakingListToSubList(headerList,H_TOP_VENDOR,H_BOT_VENDOR)
    val modelVendorTxt= modelVendor[0]

    val modelProductList=gettingDataInTag(headerList,H_TOP_PRODUCT)
    var versionProduct =getWantedString(modelProductList,' ',"version")

    /**
     * println(modelNameTxt)
     * println(modelVendorTxt)
     * println(versionProduct)
     */

    return list
}

/**
 * XMILE file Base-Level Conformance
 * 3. MUST include at least one <model> tag (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 */

fun gettingModelTagData(tokens: List<String>):String{
    //Model tokens
    val TOP_MODEL="<model>"
    val BOTTOM_MODEL="</model>"

    val modelList=breakingListToSubList(tokens,TOP_MODEL,BOTTOM_MODEL)
    //println(modelList)

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

fun gettingSimSpecsTagData(tokens: List<String>):String{
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

    //Getting data from sim_specs of XMILE format
    val simSpecs = breakingListToSubList(tokens,TOP_SIM_SPECS,BOTTOM_SIM_SPECS)
    val startOfInterval = breakingListToSubList(simSpecs,TOP_START,BOT_START)
    val endOfInterval = breakingListToSubList(simSpecs,TOP_STOP,BOT_STOP)
    val interval = breakingListToSubList(simSpecs,TOP_DT,BOT_DT)
    val simSpecsTopString = gettingDataInTag(tokens,TOP_SIM_SPECS)

    var methodSD =getWantedString(simSpecsTopString,' ',"method")

    var timeUnitSD =getWantedString(simSpecsTopString,' ',"time_units")

    var initialTime = startOfInterval[0].toFloat()

    var finalTime = endOfInterval[0].toFloat()

    var timeStep = interval[0].toFloat()

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


fun creatingListOfStrings(name: String): MutableList<String> {
    var token1 = ""
    val tokens = mutableListOf<String>()

    try {
        var fin= FileReader("src/main/kotlin/XMILE/$name.xmile")
        var c:Int?
        var x:Char
        do{
            c=fin.read()
            x=c.toChar()

            if(x=='<'){

                if( token1!=""){

                    tokens.add(token1)
                    token1=""

                }
                token1+=x

            }else if(x=='>'){

                token1+=x
                tokens.add(token1)
                token1=""

            }else{token1+=x}

        }while (c!=-1)

    }catch (ex:Exception){
        print(ex.message)
    }

    return tokens
}

/**
 * Getting attributes from specific tag
 * Functions: breakString
 *            getWantedString
 */

fun breakString(string0: String,breakPoint: Char): String {

    var tokenString = ""
    var counter = 0

    for (i in string0.indices) {

        if((string0[i] == breakPoint) && (counter == 0)){
            counter++
        }else if((string0[i] != breakPoint) && (counter == 1)){
            tokenString += string0[i]
        }else if(counter>1){
            return tokenString
        }
    }
    return tokenString
}

fun getWantedString(string0: String, breakPoint: Char, wantedString: String): String{

    var token=""
    val tokens = mutableListOf<String>()

    for (i in string0.indices) {
        if ((string0[i] == breakPoint) or (string0[i] == '>')) {
            tokens.add(token)
            token = ""
        } else {
            token += string0[i]
        }
    }

    for((index, token) in tokens.withIndex()){
        val result = token.contains(wantedString)
        if(result){
            val list1 = tokens.subList(index,index+1)
            val wantedSD = breakString(list1[0],'"')
            return wantedSD
        }
    }
   return ""
}

/***
 *  Functions for removing tags from list of strings in our case view tag:
 *                                                                   removeSlice
 *                                                                   removeWantedTag
 *                                                                   removeViewFromText
 *
 */

fun <T> removeSlice(list: MutableList<T>, from: Int, end: Int) {
    for (i in end downTo from) {
        list.removeAt(i)
    }
}

fun removingWantedTag (list0: MutableList<String>, firstBreakPoint: String, lastBreakPoint: String): MutableList<String> {



    var indexTop = mutableListOf<Int>()
    var indexBottom = mutableListOf<Int>()
    var indexHelp = 0
    var list1=list0
    var counterTop=0
    var counterBottom=0


    try{
    for ( (index, token) in list0.withIndex()) {

        val resultFirst = token.contains(firstBreakPoint)
        val resultLast = token.contains(lastBreakPoint)

        if (resultFirst) {
            indexTop.add(index)
            counterTop++

        } else if (resultLast) {
            indexBottom.add(index)
            counterBottom++

        }
    }
    if (counterTop==counterBottom){
    for(index in indexTop.indices){

        removeSlice(list1,indexTop[index],indexBottom[index])

        indexHelp = indexHelp + indexBottom[index]-indexTop[index]+1
        indexTop[index+1]=indexTop[index+1]- indexHelp
        indexBottom[index+1]=indexBottom[index+1] - indexHelp

    }}else {
      println("The function removingWantedTag would not be used for removing view because number of top and bottom tags is not same!!!")
    }
    }catch(ex:java.lang.Exception){
            print(ex.message)
        }

    return list1
}

fun removeViewFromText(tokens: MutableList<String>):MutableList<String>{
    //view ->Default simulation specifications for this model.

    val TOP_VIEW = "<views"
    val BOTTOM_VIEW ="</views>"
    val tokens1 = removingWantedTag(tokens,TOP_VIEW,BOTTOM_VIEW)
    return tokens1
}

/**
 * Functions for getting specific subtag and values between them:
 *                                                   breakingListToSublist
 *                                                   gettingDataInTag
 */

fun breakingListToSubList(list0: List<String>, firstBreakPoint: String, lastBreakPoint: String): List<String> {

    var indexTop = 0
    var indexBottom = 0

    for (  (index, token) in list0.withIndex()){

        val resultFirst = token.contains(firstBreakPoint)
        val resultLast = token.contains(lastBreakPoint)

        if (resultFirst){

            indexTop=index+1

        }else if(resultLast){

            indexBottom=index

        }
    }

    val list1 = list0.subList(indexTop,indexBottom)
    return list1
}

fun gettingDataInTag(list0: List<String>,breakPoint: String):String {

    var indexList = 0


    for (  (index, token) in list0.withIndex()){

        val result = token.contains(breakPoint)

        if(result){
            indexList=index
        }
    }

    val list1 = list0.subList(indexList,indexList+1)
    var string1= list1[0]

    return string1

}

fun Lexer(name: String){


    /**
     * Calling functions for creating tokens in Lexer
     */

    //Creating list of strings from XMILE format
    var tokens = creatingListOfStrings(name)
    tokens = removeViewFromText(tokens)
    println(tokens)

    //Getting data from XMILE tag of XMILE format
    val XMILETopString =gettingXMILETagData(tokens)
    //println(XMILETopString)

    //Getting data from header of XMILE format
    val header = gettingHeaderTagData(tokens)
    //println(header)

    //Getting data from model of XMILE format
    val model = gettingModelTagData(tokens)
    //println(model)

    //Getting data from sim_specs of XMILE format
    val sim_specs = gettingSimSpecsTagData(tokens)
    //println(sim_specs)

    //Getting data from sim_specs of XMILE format
    val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)

}




fun CreateFile(name:String,str:String) {
    try {
        var fil = FileWriter("src/main/kotlin/Kotlin_models/$name.txt",true)
        fil.write(str+"\n")
        fil.close()

    }catch(ex:java.lang.Exception){
        print(ex.message)
    }

}
fun ReadFromFile(name:String){
    try {
        var fil= FileReader("src/main/kotlin/XMILE/$name.xmile")
        var c:Int?
        do{
            c=fil.read()
            print(c.toChar())
        }while (c!=-1)
    }catch (ex:Exception){
        print(ex.message)
    }
}