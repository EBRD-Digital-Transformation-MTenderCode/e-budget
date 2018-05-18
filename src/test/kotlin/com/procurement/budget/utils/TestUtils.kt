package com.procurement.budget.utils


/**
 * Created by Maxim Sambulat.
 */

class TestUtils {

    fun getJsonFromFile(fileName: String): String {
        return this.javaClass.getResource(fileName).readText()
    }
}
