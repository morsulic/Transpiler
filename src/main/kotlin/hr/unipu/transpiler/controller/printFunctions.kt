package hr.unipu.transpiler.controller


import kotlin.reflect.jvm.internal.impl.resolve.constants.IntegerValueConstant

fun printDataInMapOfStrings(map: MutableMap<String, String>) {
    for (entry in map.entries.iterator()) {
        println(entry)

    }
}

fun printSetData(name: String, equationValueConstant: String?, equationValueConverter: String?,
                    stockInflowOutflowValue: String?, nonNegativeValue: String?, unitValue: String?, descriptionValue: String?,
                    connectionTo: String?, connectionFrom: String?, modelName: String, tagType: String
) {
    println("\n")
    println("-------$modelName $tagType-------")
    println("Name = $name")
    if(equationValueConstant!=null) println("Equation constant = $equationValueConstant")
    if(equationValueConverter!=null) println("Equation converter = $equationValueConverter")
    if(stockInflowOutflowValue!=null) println("Inflow/Outflow = $stockInflowOutflowValue")
    if(nonNegativeValue!=null) println("NonNegativeBool = $nonNegativeValue")
    if(unitValue!=null) println("Unit = $unitValue")
    if(descriptionValue!=null) println("Description = $descriptionValue")
    if(connectionTo!=null) println("ConnectionTo = $connectionTo")
    if(connectionFrom!=null) println("ConnectionFrom = $connectionFrom")

}

