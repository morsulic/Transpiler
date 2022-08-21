package hr.unipu.transpiler.controller
import java.io.FileReader

/**
 *Creating mutable list of strings from XMILE format:
 * Function: createListOfStrings
 */

fun createListOfStrings(name: String): MutableList<String> {
    var token1 = ""
    val tokens = mutableListOf<String>()

    try {
        var fin= FileReader("src/main/kotlin/hr/unipu/transpiler/XMILE/$name.xmile")
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
 *            getWantedString -> contains (breakString)
 *            getWantedStringXMILETag -> special version of getWantedString function
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

fun getWantedString(string0: String, wantedString: String): String{

    var token=""
    val tokens = mutableListOf<String>()

    for (i in string0.indices) {
        if ((string0[i] == ' ') or (string0[i] == '/') or (string0[i] == '>')) {
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

//Special function for getting data of URL xmlns in XMILE tag data
fun getWantedStringXMILETag(string0: String, wantedString: String):String{
    var token=""
    val tokens = mutableListOf<String>()

    for (i in string0.indices) {
        if ((string0[i] == ' ')  or (string0[i] == '>')) {
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

/**
 * Getting data in between opening and closing tags:
 * Function: getDataInTag
 *           getDataInTags
 */

fun getDataInTags(list0: MutableList<String>,breakPoint: String):MutableList<String> {

    var indexList = mutableListOf<Int>()
    var list1=list0
    var list2 = mutableListOf<String>()

    for (  (index, token) in list0.withIndex()){

        val result = token.contains(breakPoint)

        if(result){
            indexList.add(index)
        }
    }

    for(index in indexList.indices) {
             list1 = list0.subList(indexList[index], indexList[index] +1)
             list2 = (list2+list1) as MutableList<String>
    }

    return list2

}

fun getDataInTag(list0: List<String>,breakPoint: String):String {

    var indexList = 0
    var counter = 0

    for (  (index, token) in list0.withIndex()){

        val result = token.contains(breakPoint)

        if(result){
            indexList=index
            counter ++
        }
    }

    if(counter>0) {
        val list1 = list0.subList(indexList, indexList + 1)
        var string1 = list1[0]

        return string1
    }
    else {
        return ""
    }
}

/**
 * Functions for getting specific tag block:
 *                                         separateSameTags
 *                                         breakListToSublist
 *                                         getLowerLevelOfList
 */

fun separateSameTags(list0: MutableList<String>,firstBreakPoint: String, lastBreakPoint: String): MutableList<MutableList<String>>{

    var indexTop = mutableListOf<Int>()
    var indexBottom = mutableListOf<Int>()
    var list1=list0
    var list2 = mutableListOf<MutableList<String>>()


    for ( (index, token) in list0.withIndex()) {

        val resultFirst = token.contains(firstBreakPoint)
        val resultLast = token.contains(lastBreakPoint)

        if (resultFirst) {
            indexTop.add(index)

        } else if (resultLast) {
            indexBottom.add(index+1)

        }}

    for(index in indexTop.indices){

        list1 = list0.subList(indexTop[index],indexBottom[index])
        list2.add(list1)
    }

    return list2
}

fun breakListToSubList(list0: MutableList<String>, firstBreakPoint: String, lastBreakPoint: String): MutableList<String> {


    var indexTop = mutableListOf<Int>()
    var indexBottom = mutableListOf<Int>()
    var list1=list0
    var list2= mutableListOf<String>()
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
                indexBottom.add(index+1)
                counterBottom++

            }
        }
        if (counterTop==counterBottom){
            for(index in indexTop.indices){

                list1 = list0.subList(indexTop[index],indexBottom[index])
                list2 = (list2+list1) as MutableList<String>
            }}else {
            println("Some tag are not opened or closed properly!!!")
        }
    }catch(ex:java.lang.Exception){
        print(ex.message)
    }

    return list2
}


/**
 * Functions for arranging data names to specific namespace rules:
 *                           checkFirstChar
 *                           checkLastChar
 *                           margeNameRules->combines(checkFirstChar, checkLastChar, preparingNamesForTranspiling)
 *                           preparingNamesForTranspiling -> has function margeNamesRules (Section 3.2)
 *                           This functions will be important for base level conformance
 *                           6.   MUST obey the namespace rules (Section 2.1 and 2.2.1)
 *                           12.  MUST obey the grammar for numbers, variables, and expressions
 *                           (Sections 3.2 and 3.3, and all subsections except Sections 3.3.5 and 3.3.6).
 */

fun checkFirstChar(name: String): String{
    val validChars = listOf<Char>('q','w','e','r','t','z','u','i','o','p','a','s','d','f','g','h','j','k','l','y','x',
        'c', 'v','b','n','m','Q','W','E','R','T','Z','U','I','O','P','A','S','D','F','G','H','J','K','L','Y','X','C',
        'V','B','N','M')
   var name1 = name
    for(index in name.indices){
        if(validChars.contains(name[index])){
            return name1
        }else{
          name1 = name.removeRange(index,index+1)
        }
    }
    return name1
}
fun checkLastChar(name: String): String {
    return name.dropLastWhile { it == '_' }
}
fun removeExtraSpace(name: String): String{
    var name1=name
    for(index in name.indices){
        if(name[index]=='_'&& name[index+1]=='_'){
                name1=name1.removeRange(index,index+1)
        }
    }
    return name1
}
fun margeNameRules(name: String): String{
    var name1= name
    name1 = checkFirstChar(name1)
    name1 = checkLastChar(name1)
    name1 = removeExtraSpace(name1)
    return name1
}
fun preparingNamesForTranspiling(list0: MutableList<String>): MutableList<String>{
     var counter=1
     var name=""
     var nameHelp=""

     var operatorsAndBulitInFunctions =listOf<String>("AND","OR","NOT","IF","THEN","ELSE","STD","ABS","ARCCOS","ARCSIN",
         "ARCTAN","COS","EXP","INF","INT","LN","LOG10","MAX","MIN","PI","SIN","SQRT","TAN","EXPRND","LOGNORMAL","NORMAL",
         "POISSON","RANDOM", "DELAY","DELAY1","DELAY3","DELAY3","DELAYN","FORCST","SMTH1", "SMTH3","SMTHN","TREND",
         "PULSE","RAMP","STEP","DT","STARTTIME","STOPTIME","TIME","INIT","PREVIOUS","MOD")

     for(index in list0.indices){
         if(list0[index].contains("name=")) {
             list0[index] = list0[index].replace("\\n", "_")
             list0[index] = list0[index].replace("\\\\", "")
             list0[index] = list0[index].replace("\\\"", "")
             for (i in list0[index].indices) {

                 if (list0[index][i] == '"') {
                     counter++
                 } else if (counter % 2 == 0 && list0[index][i] == ' ') {

                     list0[index] = list0[index].substring(0, i) + '_' + list0[index].substring(i + 1)

                 } else if (list0[index][i] == '"' && counter % 2 == 0) {
                     counter++
                 }
             }
             nameHelp = getWantedString(list0[index], "name")
             name = getWantedString(list0[index], "name")
             name = margeNameRules(name)
             if (operatorsAndBulitInFunctions.contains(name.uppercase())) {
                 error("You can not use this name: $name as your name because it is bulit-in global variable!!!")
             }
             list0[index] = list0[index].replace(nameHelp, name)
         }
     }

     return list0
}

fun preparingEquationsForTranspiling(equation: String, listOfNames: MutableList<String>):String{

    return equation
}

/**
 * Function for getting model names to uppercase
 */
fun convertListOfStringToFirstCharUppercase(list: List<String>): List<String>{
    for(i in list.indices){
        list[i].lowercase().replaceFirstChar { it.uppercase()}
    }
    return list
}


