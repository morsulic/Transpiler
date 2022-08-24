package hr.unipu.transpiler.controller


import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.ls.DOMImplementationLS
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


fun parseXml(source: String): Document {
    val classloader = Thread.currentThread().contextClassLoader
    val xmlFile = classloader.getResource(source)

    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val xmlInput = InputSource(StringReader(xmlFile.readText()))
    return dBuilder.parse(xmlInput)
}

fun addNameToRootModel(root: Element) {
    val header = root.getElementsByTagName("header").item(0)
    if (header is Element) {
        val product = header.getElementsByTagName( "product").item(0)
        if (product != null) {
            val models = root.getElementsByTagName("model")
            for (i in 0 until models.length) {
                val model = models.item(i)
                if (model is Element) {
                    if (!model.hasAttribute("name")) {
                        model.setAttribute("name", product.textContent)
                    }
                }
            }
        }
    }
}

fun loadXmlWithIncludes(source: String): String {
    val tree = parseXml(source)
    val root = tree.documentElement
    addNameToRootModel(root)

    val header = root.getElementsByTagName("header").item(0)
    if (header is Element) {
        val includes = header.getElementsByTagName( "includes").item(0)
        if (includes is Element) {
            val includeList = includes.getElementsByTagName( "include")
            for (i in 0 until includeList.length) {
                val include = includeList.item(i)
                if (include is Element) {
                    if (include.hasAttribute("resource")) {
                        val resource = include.getAttribute("resource")
                        val tree = parseXml(resource)
                        val includeRoot = tree.documentElement

                        addNameToRootModel(includeRoot)
                        val header = includeRoot.getElementsByTagName("header").item(0)
                        includeRoot.removeChild(header)

                        val childNodes = includeRoot.childNodes
                        for (i in 0 until childNodes.length) {
                            val node = root.getOwnerDocument().importNode(childNodes.item(i), true)
                            root.appendChild(node)
                        }
                    }
                }
            }
        }
    }

    val document: Document = root.getOwnerDocument()
    val domImplLS = document
        .implementation as DOMImplementationLS
    val serializer = domImplLS.createLSSerializer()
    return serializer.writeToString(root)
}

