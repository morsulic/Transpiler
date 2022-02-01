package hr.unipu.transpiler.FunctionsWithFiles

import java.io.FileReader
import java.io.FileWriter

fun CreateFile(name:String,str:String) {
    try {
        var fil = FileWriter("src/main/kotlin/hr/unipu/transpiler/Kotlin_models/$name.txt",true)
        fil.write(str+"\n")
        fil.close()

    }catch(ex:java.lang.Exception){
        print(ex.message)
    }

}
fun ReadFromFile(name:String){
    try {
        var fil= FileReader("src/main/kotlin/hr/unipu/transpiler/XMILE/$name.xmile")
        var c:Int?
        do{
            c=fil.read()
            print(c.toChar())
        }while (c!=-1)
    }catch (ex:Exception){
        print(ex.message)
    }
}