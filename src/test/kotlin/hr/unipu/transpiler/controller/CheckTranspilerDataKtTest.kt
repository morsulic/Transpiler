package hr.unipu.transpiler.controller

import org.junit.Assert
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CheckTranspilerDataKtTest {

    @Test
    fun checkModelName() {
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
    }
}