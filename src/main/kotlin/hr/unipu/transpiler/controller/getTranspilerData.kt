package hr.unipu.transpiler.controller

import hr.unipu.transpiler.globalVariables.*
import org.w3c.dom.Element
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader

import javax.xml.parsers.DocumentBuilderFactory

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

fun breakString(string0: String, breakPoint: Char): String {

    var tokenString = ""
    var counter = 0

    for (i in string0.indices) {

        if ((string0[i] == breakPoint) && (counter == 0)) {
            counter++
        } else if ((string0[i] != breakPoint) && (counter == 1)) {
            tokenString += string0[i]
        } else if (counter > 1) {
            return tokenString
        }
    }
    return tokenString
}

fun getWantedString(string0: String, wantedString: String): String {

    var token = ""
    val tokens = mutableListOf<String>()

    for (i in string0.indices) {
        if ((string0[i] == ' ') or (string0[i] == '/') or (string0[i] == '>')) {
            tokens.add(token)
            token = ""
        } else {
            token += string0[i]
        }
    }

    for ((index, token) in tokens.withIndex()) {
        val result = token.contains(wantedString)
        if (result) {
            val list1 = tokens.subList(index, index + 1)
            val wantedSD = breakString(list1[0], '"')
            return wantedSD
        }
    }
    return ""
}


fun getName(element: Element, tagName: String): String {
    if (!element.hasAttribute("name")) error("$tagName missing 'name' attribute")
    return transformName(element.getAttribute("name"))
}

fun getWantedTagValue(element: Element, tagIdentifier: String): String? {
    val tag = element.getElementsByTagName(tagIdentifier).item(0)
    if (tag != null) {
        return tag.textContent
    }
    return null
}

fun getWantedTagValues(element: Element, tagIdentifier: String): List<String>? {
    val tags = element.getElementsByTagName(tagIdentifier)
    val tagLength = tags.length
    var tagList = listOf<String>()
    if (tags != null) {
        for (i in 0 until tagLength) {
            tagList = listOf(tags.item(i).textContent)
        }
        return tagList
    }
    return null
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


fun getXmileData(element: Element): List<Map<String, Any>> {
    var tModel = listOf<Map<String, Any>>()

    getXmileTagData(element)
    getSimSpecs(element)
    getHeader(element)
    tModel = listOf(getModels(element))

    return tModel
}

/**
 * XMILE file Base-Level Conformance
 * 1. MUST include an <xmile> tag that contains both the version of XMILE used and the XMILE XML namespace (Section 2)
 * http://docs.oasis-open.org/xmile/xmile/v1.0/errata01/csprd01/xmile-v1.0-errata01-csprd01-complete.html#_Toc442104247
 * Function: getXmileTagData()
 */

fun getXmileTagData(element: Element) {
    if (!element.hasAttribute("version")) error("XMILE needs to have attribute version!")
    if (!element.hasAttribute("xmlns")) error("XMILE needs to have attribute xmlns!")

    val version = element.getAttribute("version").toString()
    val xmlns = element.getAttribute("xmlns").toString()

    if (version == "") error("XMILE version must be added in XMILE tag.")
    if (xmlns == "") error("XMILE namespace must be added in XMILE tag.")

    transpilerDataMap += mapOf("Version" to version)
    transpilerDataMap += mapOf("xmlns" to xmlns)
}

fun getSimSpecs(element: Element, modelName: String = "default") {
    val simSpec = element.getElementsByTagName("sim_specs").item(0) as Element?
    if (simSpec != null) {
        var method = simSpec.getAttribute("method")
        val timeUnit = simSpec.getAttribute("time_units")
        val startTime = simSpec.getElementsByTagName("start").item(0).textContent
        val stopTime = simSpec.getElementsByTagName("stop").item(0).textContent
        val dt = simSpec.getElementsByTagName("dt").item(0).textContent

        method = prepareTypeOfSimSpecsIntegration(method)

        transpilerDataMap += "$modelName SimSpecs Method:" to "$method"
        transpilerDataMap += "$modelName SimSpecs Time unit:" to "$timeUnit"
        transpilerDataMap += "$modelName SimSpecs Initial time:" to "$startTime"
        transpilerDataMap += "$modelName SimSpecs Final time:" to "$stopTime"
        transpilerDataMap += "$modelName SimSpecs Time step:" to "$dt"
    } else {
        transpilerDataMap += mutableMapOf("$modelName SimSpecs empty:" to modelName)
    }
}

fun getHeader(element: Element) {
    val headers = element.getElementsByTagName("header").item(0) as Element?
    if (headers != null) {

        modelRootNameConst = headers.getElementsByTagName("name").item(0).textContent
        val product = headers.getElementsByTagName("product").item(0) as Element?
        val productName = headers.getElementsByTagName("product").item(0).textContent

        if (product != null) {
            val productVersion = product.getAttribute("version")
            transpilerDataMap += mapOf("Product version" to productVersion)
        }

        val vendor = headers.getElementsByTagName("vendor").item(0).textContent
        transpilerDataMap += mapOf("Header root model name" to modelRootNameConst)
        transpilerDataMap += mapOf("Vendor" to vendor)
        transpilerDataMap += mapOf("Product name" to productName)
    } else {
        error("XMILE document needs to have et least one header tag.")
    }
}

fun getModels(element: Element): Map<String, Any> {
    val models = element.getElementsByTagName("model")
    val modelNameList = mutableListOf<String>()
    var map = mapOf<String, Any>()
    for (i in 0 until models.length) {
        val model = models.item(i) as Element

        var modelName = checkModelName(model, i)
        modelNameList.add(i, modelName)
        getSimSpecs(model, modelName)
        map += mapOf(modelName to getStockList(model, modelName))
        map += mapOf(modelName to getFlowList(model, modelName))
        map += mapOf(modelName to getAuxList(model, modelName))
        map += mapOf(modelName to getModules(model, modelName))
    }
    transpilerDataMap += mapOf("ModelNamesListInStringForm" to "" + modelNameList.toString())
    return map
}

fun getStocks(element: Element, modelName: String): Map<String, String?> {
    val stock = element
    val name = getName(stock, "Stock")
    if (name.isEmpty()) error("Stock 'name' attribute is empty")

    val eqn = getWantedTagValue(stock, "eqn")
    val inflowList = getWantedTagValues(stock, "inflow")
    val outflowList = getWantedTagValues(stock, "outflow")
    val unit = getWantedTagValue(stock, "units")
    val description = getWantedTagValue(stock, "doc")
    var inflow = ""
    var outflow = ""

    transpilerDataMap += mapOf("$modelName StockName: $name" to "$name")
    if (eqn != null) {
        prepareEquationsInConstantsOrConverters(modelName, name, "Stock", eqn)
        //transpilerDataMap += mapOf("$modelName StockEquationTokenValue: $name" to "$eqn")
    }
    if (inflowList != null) inflow = prepareInflowsOfStock(inflowList)


    if (outflowList != null) outflow = prepareOutflowsOfStock(outflowList)

    if(inflow.isNotEmpty() || outflow.isNotEmpty())
        transpilerDataMap += mapOf("$modelName StockInflowOutflowValue: $name" to "$inflow$outflow")

    if (unit != null) transpilerDataMap += mapOf("$modelName StockUnitValue: $name" to "$unit")
    if (description != null) transpilerDataMap += mapOf("$modelName StockDescriptionValue: $name" to "$description")

    return mapOf("name" to name, "eqn" to eqn)
}

fun getStockList(element: Element, modelName: String): List<Map<String, String?>> {

    val stocks = element.getElementsByTagName("stock")
    var stockList = listOf<Map<String, String?>>()

    for (i in 0 until stocks.length) {
        val stock = stocks.item(i) as Element
        stockList += getStocks(stock, modelName)
    }
    return stockList
}


fun getFlows(element: Element, modelName: String): Map<String, String?> {
    val flow = element
    val name = getName(flow, "Flow")
    if (name.isEmpty()) error("Flow 'name' attribute is empty")

    val eqn = getWantedTagValue(flow, "eqn")
    val unit = getWantedTagValue(flow, "units")
    val description = getWantedTagValue(flow, "doc")

    transpilerDataMap += mapOf("$modelName FlowName: $name" to "$name")
    if (eqn != null) {
        prepareEquationsInConstantsOrConverters(modelName, name, "Flow", eqn)
    }
    if (unit != null) transpilerDataMap += mapOf("$modelName FlowUnitValue: $name" to "$unit")
    if (description != null) transpilerDataMap += mapOf("$modelName FlowDescriptionValue: $name" to "$description")

    return mapOf("name" to name, "eqn" to eqn)
}

fun getFlowList(element: Element, modelName: String): List<Map<String, String?>> {

    val flows = element.getElementsByTagName("flow")
    var flowList = listOf<Map<String, String?>>()
    for (i in 0 until flows.length) {
        val flow = flows.item(i) as Element
        flowList += getFlows(flow, modelName)
    }
    return flowList
}


fun getAux(element: Element, modelName: String): Map<String, String?> {
    val aux = element
    val name = getName(aux, "Aux")
    if (name.isEmpty()) error("Aux 'name' attribute is empty")

    transpilerDataMap += mapOf("$modelName AuxName: $name" to "$name")
    val eqn = getWantedTagValue(aux, "eqn")
    val unit = getWantedTagValue(aux, "units")
    val description = getWantedTagValue(aux, "doc")

    if (eqn != null) {
        prepareEquationsInConstantsOrConverters(modelName, name, "Aux", eqn)
        //transpilerDataMap += mapOf("$modelName AuxEquationTokenValue: $name" to "$eqn")
    }
    if (unit != null) transpilerDataMap += mapOf("$modelName AuxUnitValue: $name" to "$unit")
    if (description != null) transpilerDataMap += mapOf("$modelName AuxDescriptionValue: $name" to "$description")

    return mapOf("name" to name, "eqn" to eqn)

}

fun getAuxList(element: Element, modelName: String): List<Map<String, String?>> {
    val auxes = element.getElementsByTagName("aux")
    var auxList = listOf<Map<String, String?>>()
    for (i in 0 until auxes.length) {
        val aux = auxes.item(i) as Element
        auxList += getAux(aux, modelName)
    }
    return auxList
}

fun getModules(element: Element, modelName: String) {
    val modules = element.getElementsByTagName("module")
    for (i in 0 until modules.length) {
        val module = modules.item(i) as Element
        val name = getName(module, "Module")
        if (name.isEmpty()) error("Module 'name' attribute is empty")

        transpilerDataMap += mapOf("$modelName ModuleName: $name" to "$name")
        val connections = module.getElementsByTagName("connect")
        for (i in 0 until connections.length) {
            val connect = connections.item(i) as Element
            val connectTo = connect.getAttribute("to")
            val connectFrom = connect.getAttribute("from")
            transpilerDataMap += mapOf("$modelName ModuleConnectionsTo: $name" to "$connectTo")
            transpilerDataMap += mapOf("$modelName ModuleConnectionsFrom: $name" to "$connectFrom")
        }
        transpilerDataMap += mapOf("$modelName ModelNameConnectionsLength: $name" to "${connections.length}")

    }

}


fun main() {
    val classloader = Thread.currentThread().contextClassLoader
    val xmlFileName = "proba.xml"
    val xmlFile = classloader.getResource(xmlFileName)
    val tree = parseXml(xmlFile.readText())
    val root = tree.documentElement

    //getXmileData(root)
    val tModel = getXmileData(root)
    //setConstants(constants)

    //generateKt()
    printDataInMapOfStrings(transpilerDataMap)
    transpilerDataMap = emptyMap
    modelRootNameConst = ""
    val ktFileName = "src/main/kotlin/MyModel.kt"
    //Files.write(Paths.get(ktFileName), KT_STR.encodeToByteArray())
}
