

/***
 *  Functions for removing tag blocks from list of strings in our case view tag:
 *                                                                   removeSlice
 *                                                                   removeWantedTagBlock -> contains(removeSlice)
 */

fun <T> removeSlice(list: MutableList<T>, from: Int, end: Int) {
    for (i in end downTo from) {
        list.removeAt(i)
    }
}

fun removeWantedTagBlock (list0: MutableList<String>, firstBreakPoint: String, lastBreakPoint: String): MutableList<String> {



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

