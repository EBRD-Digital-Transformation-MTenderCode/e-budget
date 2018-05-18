package com.procurement.budget.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.junit.jupiter.api.Assertions
import java.io.StringWriter


private object JsonMapper {
    val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        mapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
    }
}

fun <T> compare(target: Class<T>, file: String) {
    val resource = getJsonFromFile(file)
    val obj = toObject(target, resource)
    val actual = toJson(obj)
    Assertions.assertEquals(resource, actual)
}

fun getJsonFromFile(fileName: String): String {
    return toCompact(JsonMapper.javaClass.getResource(fileName).readText())
}

private fun toCompact(source: String): String {
    val factory  = JsonMapper.mapper.factory
    val parser = factory.createParser(source)
    val out = StringWriter()
    factory.createGenerator(out).use { gen ->
        while (parser.nextToken() != null) {
            gen.copyCurrentEvent(parser)
        }
    }
    return out.buffer.toString()
}
