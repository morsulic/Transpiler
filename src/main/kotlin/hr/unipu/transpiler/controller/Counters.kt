package hr.unipu.transpiler.controller

/**
 * Function for counting number of specific tags:
 *                                          countTags
 */

fun countTags(list0: MutableList<String>,firstBreakPoint: String, lastBreakPoint: String): Int {
    var counterTop=0
    var counterBottom=0

    try{
        for ( (index, token) in list0.withIndex()) {

            val resultFirst = token.contains(firstBreakPoint)
            val resultLast = token.contains(lastBreakPoint)

            if (resultFirst) {
                counterTop++

            } else if (resultLast) {
                counterBottom++

            }
        }}
        catch(ex:java.lang.Exception){
            print(ex.message)
        }

        if (counterTop==counterBottom){
        return counterTop
        }
        else {
            println("Tags are not closed properly!!!")
            return 0
        }
}

