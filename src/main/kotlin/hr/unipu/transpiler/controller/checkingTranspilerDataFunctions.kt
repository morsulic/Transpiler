package hr.unipu.transpiler.controller

/**
* 1. Function for checking if tag is added
*        checkingNameListIsAdded
*/

fun checkingTagListIsAdded(tag: List<String>, tagName: String): Boolean {
    if (tag.isNotEmpty()) {
        return true
    }
    error("Error: $tagName not properly configured!!!")
}
/**
 * 2. Function for checking is name or equation is added in tag
 *        checkingNameIsAdded
 */

fun checkingTagIsAdded(tag: String, tagName: String): Boolean {
    if (tag.isNotEmpty()) {
        return true
    }
    error("Error: $tagName value must not be empty!!!")
}









