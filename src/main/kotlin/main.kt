
import hr.unipu.transpiler.ModelTemplate.getKSDTemplate
import hr.unipu.transpiler.controller.*


fun main() {
    //ReadFromFile("hares_and_foxes")
    //Lexer("hares_and_foxes")
    Lexer("Vacation")

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

    //Preparing names for transpiling
    tokens = preparingNamesForTranspiling(tokens)

    //Removing view tags if possible
    tokens = removeWantedTagBlock(tokens,"<views","</views>")
    tokens.remove("<model_units>")
    tokens.remove("<model_units/>")
    //println(tokens)
    //println("\n")


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
    //println("\n")
    //2. Getting data from header of hr.unipu.transpiler.XMILE format
    val headerMap = gettingHeaderTagData(tokens)
    val modelName = headerMap.getValue("Model name").toString()
    //println(headerMap)
    //println("\n")
    //3. Getting data from model of hr.unipu.transpiler.XMILE format
    val model = gettingModelTagData(tokens,modelName)
    transpiledModelsData(model)
    println("\n")
    //Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    val simSpecsMap = gettingSimSpecsTagData(tokens)
    //println(simSpecsMap)
    //println("\n")
    //Getting data from options of hr.unipu.transpiler.XMILE format
    val options = gettingOptionsTagData(tokens)
    //println(options)

    //Getting data from behaviour of hr.unipu.transpiler.XMILE format
    val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)



}




