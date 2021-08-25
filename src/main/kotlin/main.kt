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
        "    }}" //----------------------------------------------------------------->POSLJE MAKNI TU dodatnu zagradu
var initializationOfModel=""
var a = descriptionPackage+constantValues

fun main() {
   // ReadFromFile("hares_and_foxes")
   //Lexer("hares_and_foxes")
   CreateFile("proba",a)
}


fun creatingListOfStrings(name: String): MutableList<String> {
    var token1 = ""
    val tokeni = mutableListOf<String>()

    try {
        var fin= FileReader("src/main/kotlin/XMILE/$name.xmile")
        var c:Int?
        var x:Char
        do{
            c=fin.read()
            x=c.toChar()

            if(x=='<'){

                if( token1!=""){

                    tokeni.add(token1)
                    token1=""

                }
                token1+=x

            }else if(x=='>'){

                token1+=x
                tokeni.add(token1)
                token1=""

            } else{token1+=x}

        }while (c!=-1)

    }catch (ex:Exception){
        print(ex.message)
    }

    return tokeni
}
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
    val tokeni = mutableListOf<String>()
    for (i in string0.indices) {
        if(string0[i] == breakPoint){
            tokeni.add(token)
            token=""
        }else{
            token+=string0[i]
        }
    }
    for((index, token) in tokeni.withIndex()){
        val result = token.contains(wantedString)
        if(result){
            val list1 = tokeni.subList(index,index+1)
            val methodSD = breakString(list1[0],'"')
            return methodSD
        }
    }
   return ""
}

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

fun getingDataInTag(list0: List<String>,breakPoint: String,):String {

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
     * Tags from XMILE format that we use for Transpiling (Creating Lexer in this case).
     */

    //XMILE provjera verzije (trenutno jedina verzija 1.0)
    val sXMILE ="<xmile"
    val VERSION ="version"

    //Header tokens
    val TOP_HEADER ="<header>"
    val BOTTOM_HEADER ="</header>"
    val H_TOP_NAME ="<name>"
    val H_BOT_NAME ="</name>"

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


    /**
     * Calling functions for creating tokens in Lexer
     */


    //Creating list of strings from XMILE format
    val tokeni = creatingListOfStrings(name)


    //Getting data from header of XMILE format
    val headerList=breakingListToSubList(tokeni,TOP_HEADER,BOTTOM_HEADER)
    val modelName=breakingListToSubList(headerList,H_TOP_NAME,H_BOT_NAME)
    var modelNameTxt= modelName[0]
    println(modelNameTxt)


    //Getting data from sim_specs of XMILE format
    val simSpecs = breakingListToSubList(tokeni,TOP_SIM_SPECS,BOTTOM_SIM_SPECS)
    val startOfInterval = breakingListToSubList(simSpecs,TOP_START,BOT_START)
    val endOfInterval = breakingListToSubList(simSpecs,TOP_STOP,BOT_STOP)
    val interval = breakingListToSubList(simSpecs,TOP_DT,BOT_DT)
    val simSpecsTopString = getingDataInTag(tokeni,TOP_SIM_SPECS)

    var methodSD =getWantedString(simSpecsTopString,' ',"method")
    println(methodSD)

    var timeUnitSD =getWantedString(simSpecsTopString,' ',"time_units")
    println(timeUnitSD)

    var initialTime = startOfInterval[0].toFloat()
    println(initialTime)

    var finalTime = endOfInterval[0].toFloat()
    println(finalTime)

    var timeStep = interval[0].toFloat()
    println(timeStep)
}


fun Parser(){

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