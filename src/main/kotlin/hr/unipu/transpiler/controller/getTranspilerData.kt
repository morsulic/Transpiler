package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.*
import org.w3c.dom.Element
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import java.lang.Exception
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

const val ATTR_NAME = "name"
const val TAG_AUX = "aux"
const val TAG_EQU = "equ"

var KT_STR = ""

val STATIC_PROPERTIES = mutableListOf<Pair<String, Any>>()

var modelRootNameConst = ""

fun parseXml(xmlStr: String): Document {
    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val xmlInput = InputSource(StringReader(xmlStr))
    return dBuilder.parse(xmlInput)
}

fun transformName(s: String): String {
    return s.filter { !it.isWhitespace() }
}


fun getWantedTagValue(element: Element, tagIdentifier: String): String {
    val tag = element.getElementsByTagName(tagIdentifier).item(0)
    if (tag != null) {
        return tag.textContent
    }
    return ""
}

fun getInflowsValue(element: Element): String {
    val inflow = element.getElementsByTagName("inflow")
    var inflowList = listOf<String>()
    for (i in 0 until inflow.length) {
        val inflowValue = element.getElementsByTagName("inflow").item(0)
        if (inflow != null) {
            inflowList += listOf(inflowValue.toString())
        }
    }
    return prepareInflowsOfStock(inflowList)
}

fun getOutFlowsValue(element: Element): String {
    val outflow = element.getElementsByTagName("outflow")
    var outflowList = listOf<String>()
    for (i in 0 until outflow.length) {
        val outflowValue = element.getElementsByTagName("inflow").item(0)
        if (outflow != null) {
            outflowList += listOf(outflowValue.toString())
        }
    }
    return prepareOutflowsOfStock(outflowList)
}


fun getXmileData(element: Element) {
    getHeader(element)
    getModels(element)
}

fun getHeader(element: Element) {
    val header = element.getElementsByTagName("header").item(0) as Element
    modelRootNameConst = header.getElementsByTagName("name").item(0).textContent
}

fun getModels(element: Element) {
    val models = element.getElementsByTagName("model")
    for (i in 0 until models.length) {
        val model = models.item(i) as Element

        var modelName = model.getAttribute("name")

        if (!model.hasAttribute("name") && i > 0) error("Model missing 'name' attribute")
        else if (!model.hasAttribute("name") && i == 0) modelName = modelRootNameConst
        if (modelName == null) error("Model 'name' attribute is empty!")
        getStocks(model, modelName)
        getFlows(model, modelName)
        getAux(model, modelName)
    }
}

fun getStocks(element: Element, modelName: String) {

    val stocks = element.getElementsByTagName("stock")
    for (i in 0 until stocks.length) {
        val stock = stocks.item(i) as Element

        if (!stock.hasAttribute("name")) error("Stock missing 'name' attribute")
        val name = transformName(stock.getAttribute("name"))
        if (name.isEmpty()) error("Stock 'name' attribute is empty")

        val eqn = stock.getElementsByTagName("eqn").item(0)
        val inflow = stock.getElementsByTagName("inflow").item(0)
        val outflow = stock.getElementsByTagName("outflow").item(0)
        val unit = stock.getElementsByTagName("units").item(0)
        val description = stock.getElementsByTagName("doc").item(0)

        transpilerDataMap += mapOf("$modelName StockName: $name" to "$name")
        if (eqn != null) transpilerDataMap += mapOf("$modelName StockEquationTokenValue: $name" to "${eqn.textContent}")
        if (inflow != null) transpilerDataMap += mapOf("$modelName StockInflowValue: $name" to "${inflow.textContent}")
        if (outflow != null) transpilerDataMap += mapOf("$modelName StockOutflowValue: $name" to "${outflow.textContent}")
        if (unit != null) transpilerDataMap += mapOf("$modelName StockUnitValue: $name" to "${unit.textContent}")
        if (description != null) transpilerDataMap += mapOf("$modelName StockDescriptionValue: $name" to "${description.textContent}")

    }
}

fun getFlows(element: Element, modelName: String) {

    val flows = element.getElementsByTagName("flows")
    for (i in 0 until flows.length) {
        val flow = flows.item(i) as Element

        if (!flow.hasAttribute("name")) error("Flow missing 'name' attribute")
        val name = transformName(flow.getAttribute("name"))
        if (name.isEmpty()) error("Flow 'name' attribute is empty")

        val eqn = flow.getElementsByTagName("eqn").item(0)
        val unit = flow.getElementsByTagName("units").item(0)
        val description = flow.getElementsByTagName("doc").item(0)

        transpilerDataMap += mapOf("$modelName FlowName: $name" to "$name")
        if (eqn != null) transpilerDataMap += mapOf("$modelName FlowEquationTokenValue: $name" to "${eqn.textContent}")
        if (unit != null) transpilerDataMap += mapOf("$modelName FlowUnitValue: $name" to "${unit.textContent}")
        if (description != null) transpilerDataMap += mapOf("$modelName FlowDescriptionValue: $name" to "${description.textContent}")

    }
}

fun getAux(element: Element, modelName: String) {
    val auxes = element.getElementsByTagName("aux")
    for (i in 0 until auxes.length) {
        val aux = auxes.item(i) as Element
        if (!aux.hasAttribute("name")) error("Aux missing 'name' attribute")

        val name = transformName(aux.getAttribute("name"))
        if (name.isEmpty()) error("Aux 'name' attribute is empty")

        transpilerDataMap += mapOf("$modelName AuxName: $name" to "$name")
        val eqn = aux.getElementsByTagName("eqn").item(0)
        val unit = aux.getElementsByTagName("units").item(0)
        val description = aux.getElementsByTagName("doc").item(0)

        if (eqn != null) transpilerDataMap += mapOf("$modelName AuxEquationTokenValue: $name" to "${eqn.textContent}")
        if (unit != null) transpilerDataMap += mapOf("$modelName AuxUnitValue: $name" to "${unit.textContent}")
        if (description != null) transpilerDataMap += mapOf("$modelName AuxDescriptionValue: $name" to "${description.textContent}")

    }
}


fun setConstants(auxConstantPairs: List<Pair<String, BigDecimal>>) {
    for ((aux, _) in auxConstantPairs) {
        val auxNameUpper = aux.uppercase()
        STATIC_PROPERTIES.add(Pair("${auxNameUpper}_KEY", "${auxNameUpper}"))
    }

    for ((aux, constant) in auxConstantPairs) {
        val auxNameUpper = aux.uppercase()
        STATIC_PROPERTIES.add(Pair("${auxNameUpper}_VALUE", constant))
    }

    // TODO: init

    // TODO: equation
}

fun generateStaticProperties(staticProperties: String): String {
    return ""
}

fun generateKt(staticProperties: String, init: String) {
    println("import hr.unipu.ksdtoolkit.entities.Model")
    println("open class MyModel : Model() {")
    generateStaticProperties(staticProperties)
    println("}")
}

fun main() {
    val classloader = Thread.currentThread().contextClassLoader
    val xmlFileName = "proba.xml"
    val xmlFile = classloader.getResource(xmlFileName)
    val tree = parseXml(xmlFile.readText())
    val root = tree.documentElement

    //getXmileData(root)
    getXmileData(root)
    //setConstants(constants)

    //generateKt()
    printingDataInMapOfStrings(transpilerDataMap)
    transpilerDataMap = emptyMap
    modelRootNameConst = ""
    val ktFileName = "src/main/kotlin/MyModel.kt"
    Files.write(Paths.get(ktFileName), KT_STR.encodeToByteArray())
}
