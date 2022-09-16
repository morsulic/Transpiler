import hr.unipu.transpiler.controller.*
import hr.unipu.transpiler.globalVariables.transpilerDataMap
import hr.unipu.transpiler.globalVariables.transpilerDataMapKsdToolkitSet
import hr.unipu.transpiler.globalVariables.transpilerString
import org.w3c.dom.Element


fun main() {
   /* var tokens = createListOfStrings("Vacation")
    tokens = prepareNamesForTranspiling(tokens)
    tokens = removeWantedTagBlock(tokens,"<views","</views>")
    var connectString = ""
    for (token in tokens){
        connectString += token.removeSurrounding(",",",")
    }
    println(connectString)

    createFile( "Vacation1",connectString)*/

    val classloader = Thread.currentThread().contextClassLoader
    val xmlFileName = "Vacation1.xml"
    val xmlFile = classloader.getResource(xmlFileName)
    val tree = parseXml(xmlFile.readText())
    val root = tree.documentElement

    transpilerXmileToKsdtoolkit(root)
    /**val source = "main.xml"
    print(loadXmlWithIncludes(source))
    return*/

    //ReadFromFile("hares_and_foxes")
    //transpiler("hares_and_foxes")
    //transpilerOld("Vacation")
    //transpiler("Comments_test")

    /**
     * Testing functionality of removing view tag
     * Tests 1 to 3
     */

    //transpiler("Test1RemoveView")// Main test for remove view +
    //transpiler("Test2RemoveViewOneMore") //Testing removing one more view block +
    //transpiler("Test3RemoveViewErrorTags") //Testing remove view with one view block without end tag

    /**
     * Testing functionality of Base-Level Conformance 1. MUST include an <xmile> tag that contains both the version of
     * XMILE used and the XMILE XML namespace (Section 2)
     */
    //transpiler("Test6WithOutXMILETag") //Testing  XMILE tag missing
    //transpiler("Test7WithOutXMILETag") //Testing  XMILE tag version missing
    //transpiler("Test8WithOutXMILETag") //Testing  XMILE tag namespace missing

    /**
     * Testing functionality of Base-Level Conformance  2. MUST include a <header> tag (Section 2) with sub-tags
     * <vendor> and <product> with its version number (Section 2.2)
     */
    //transpiler("Test9WithOutHeaderTag") //Testing header tag missing
    //transpiler("Test10WithOutHeaderTag") //Testing header tag name missing
    //transpiler("Test11WithOutHeaderTag") //Testing header tag product version missing
    //transpiler("Test16WithOutHeaderTag") //Testing header tag product version value missing
    /**
     * Testing functionality of Base-Level Conformance 3. MUST include at least one <model> tag (Section 2) and
     * 4. MUST name models beyond the root model (Section 4)
     */
    //transpiler("Test4WithOutModel") //Testing that at least one model tag must exist in XMILE format
    //transpiler("Test17WithOutSubModelName") //Testing that all submodels are properly named

    /**
     * Testing functionality of Base-Level Conformance 7. MUST include, when using optional features, the <options>
     * tag with those features specified (Section 2.2.1)
     */
    //transpiler("Test15OptionsUsesSubModels")


    /**
     * Testing functionality of Base-Level Conformance  8. MUST contain at least one set of simulation
     * specifications (Section 2.3)
     */
    //transpiler("Test12WithOutSimSpecs") //Testing that at least one sim_specs tag must exist in XMILE format
    //transpiler("Test13WithOutSimSpecs") //Testing sim_specs start of interval tag value missing
    //transpiler("Test14WithOutSimSpecs") //Testing sim_specs dt tag value missing used default value time step = 1.0
    //transpiler("Test18WithOutSimSpecs") //Testing is additional sim_specs saved in specialized model
    /**
     * Testing functionality of Base-Level Conformance 9. MUST support model behaviors (Section 2.6)
     */
    //transpiler("Test5BehaviorTag") //Testing getting data from behaviour tag

    /**
     * Creating test file
     */
    // val KSDmodel= getKSDTemplate() //uncomment the import
    //CreateFile("proba1", KSDmodel) //uncomment the import
}

/**
 * Creating tokens and removing wanted values with lexer function
 */

fun transpilerXmileToKsdtoolkit(root: Element) {
    getXmileData(root)
    printDataInMapOfStrings(transpilerDataMap)
    printDataInMapOfStrings(transpilerDataMapKsdToolkitSet)
    setDataForTranspiling()
    //printDataInMapOfStrings(transpilerDataMapKsdToolkitSet)
    transpilerStringCreator()
    println(transpilerString)
    transpilerString=null

}
/**
fun transpilerOld(name: String) {


    /**
     * Generating wanted token blocks by calling functions:
     *                                              createListOfStrings
     *                                              removeViewFromText
     */

    //Creating list of strings (token blocks) from hr.unipu.transpiler.XMILE format
    var tokens = createListOfStrings(name)
    //println(tokens)
    //Preparing names for transpiling
    tokens = preparingNamesForTranspiling(tokens)

    //Removing view tags if possible
    tokens = removeWantedTagBlock(tokens, "<views", "</views>")
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
    gettingXMILETagData(tokens)
    //println(tokens)
    //2. Getting data from header of hr.unipu.transpiler.XMILE format
    //gettingHeaderTagData(tokens)
    var modelName = transpilerDataMap.getValue("Model name").toString()

    //3. Getting data from sim_specs of hr.unipu.transpiler.XMILE format
    gettingSimSpecsTagData(tokens)

    //4. Getting data from model of hr.unipu.transpiler.XMILE format
    gettingModelTagData(tokens, modelName)
    println("\n")

    //Getting data from options of hr.unipu.transpiler.XMILE format
    gettingOptionsTagData(tokens)


    //Getting data from behaviour of hr.unipu.transpiler.XMILE format
    val behavior = gettingBehaviorTagData(tokens)
    //println(behavior)
    //transpiledModelsData(tModels)
    printingDataInMapOfStrings(transpilerDataMap)
    settingDataForTranspiling()
    printingDataInMapOfStrings(transpilerDataMapKsdToolkitSet)
    transpilerStringCreator()
    println(transpilerString)

}
*/



