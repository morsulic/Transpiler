package hr.unipu.transpiler.model

class TranspilerStock {
    var stockName: String = ""
    var inflows:  MutableList<String> = mutableListOf()
    var outflows: MutableList<String> = mutableListOf()
    var initialValueEquation: String = ""
    var nonNegative: Boolean = false
}