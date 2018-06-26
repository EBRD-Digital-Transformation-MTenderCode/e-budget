package com.procurement.budget.model.dto.databinding

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.exception.ErrorType
import java.io.IOException

class BooleanDeserializer : JsonDeserializer<Boolean>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Boolean {
        if (jsonParser.currentToken != JsonToken.VALUE_FALSE || jsonParser.currentToken != JsonToken.VALUE_TRUE) {
            throw ErrorException(ErrorType.INVALID_JSON_TYPE, jsonParser.currentName)
        }
        return jsonParser.valueAsBoolean
    }
}