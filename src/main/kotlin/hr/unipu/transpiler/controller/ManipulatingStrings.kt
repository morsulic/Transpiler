package hr.unipu.transpiler.hr.unipu.transpiler.controller
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


/**
 * Getting data in between opening and closing tags:
 * Function: getDataInTag
 */

fun getDataInTag(list0: List<String>,breakPoint: String):String {

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