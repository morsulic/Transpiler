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
        if ((string0[i] == breakPoint) or (string0[i] == '/') or (string0[i] == '>')) {
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
 * Function for getting specific tag block:
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

fun getLowerLevelOfList(list0: MutableList<String>, firstBreakPoint: String, lastBreakPoint: String): MutableList<String> {


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
                indexTop.add(index+2)
                counterTop++

            } else if (resultLast) {
                indexBottom.add(index-1)
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


