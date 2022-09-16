package hr.unipu.transpiler.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

fun inputWantedTestDataXml(name:String): org.w3c.dom.Element {
    val classloader = Thread.currentThread().contextClassLoader
    val xmlFileName = name
    val xmlFile = classloader.getResource(xmlFileName)
    val tree = parseXml(xmlFile.readText())
    val root = tree.documentElement
    return  root
}

internal class CheckTranspilerDataKtTest {

    val test1 = inputWantedTestDataXml("checkModelNameTest/test1CheckModelName.xml")
    val test2 = inputWantedTestDataXml("checkModelNameTest/test2CheckModelName.xml")
    val test3 = inputWantedTestDataXml("checkModelNameTest/test3CheckModelName.xml")

    @Test
    fun checkModelNameTest() {
        getHeader(test1) //Needs to be called before function checkModelName because header needs to be gotten first
        //Test if non root model has a name
        assertThrows(IllegalStateException::class.java, {checkModelName(test1,1)},"Model missing 'name' attribute.")
        //Test if root model has a name it must not have it
        assertThrows(IllegalStateException::class.java, {checkModelName(test2,1)},"Root model MUST NOT have attribute 'name'.")
        //Test if root model does not have a name
        assertEquals("Vacation", checkModelName(test1,0))
        //Test if root model has a name it must not have it
        assertThrows(IllegalStateException::class.java, {checkModelName(test3,1)},"Model 'name' attribute is empty!")
    }

    @Test
    fun checkAlphaOrUnderscoreTest() {
        //Test if parameter char=a -> return true
        assertTrue(checkAlphaOrUnderscore('a'))
        //Test if parameter char=_  -> return true
        assertTrue(checkAlphaOrUnderscore('_'))
        //Test if parameter char=% -> return false
        assertFalse(checkAlphaOrUnderscore('%'))
    }

    @Test
    fun checkContainsMacroFunction() {
        //Test if eqn tag value is null
        assertFalse(checkContainsMacroFunction(null))
        //Test if eqn tag value is not Built-In function
        assertFalse(checkContainsMacroFunction("20 - ABC"))
        //Test if eqn tag value is Built-In function
        assertTrue(checkContainsMacroFunction("PULSE(number_of_assignments_added_1*DT, 2,0 )"))
    }
}