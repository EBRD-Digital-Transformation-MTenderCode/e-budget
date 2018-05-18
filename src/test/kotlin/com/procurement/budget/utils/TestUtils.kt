package com.procurement.budget.utils

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import org.junit.jupiter.api.Assertions
import java.io.StringWriter


object TestUtils

fun <T> compare(target: Class<T>, file: String) {
    val resource = getJsonFromFile(file)
    val obj = toObject(target, resource)
    val actual = toJson(obj)
    Assertions.assertEquals(resource, actual)
}

fun getJsonFromFile(fileName: String): String {
   return toCompact(TestUtils.javaClass.getResource(fileName).readText())
}

private fun toCompact(source: String): String {
    val factory = JsonFactory()
    factory.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS)
    val parser = factory.createParser(source)
    val out = StringWriter()
    factory.createGenerator(out).use { gen ->
        while (parser.nextToken() != null) {
            gen.copyCurrentEvent(parser)
        }
    }
    return out.buffer.toString()
}
