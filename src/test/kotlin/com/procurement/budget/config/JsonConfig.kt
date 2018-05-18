package com.procurement.budget.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.springframework.context.annotation.Configuration
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.annotation.PostConstruct

@Configuration
class JsonConfig {

    @PostConstruct
    fun init() {
        JsonMapper.init()
        DateFormatter.init()
    }

    object JsonMapper {
        lateinit var mapper: ObjectMapper
        fun init() {
            mapper = ObjectMapper()
            mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
            mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            mapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)

        }
    }

    object DateFormatter {
        lateinit var formatter: DateTimeFormatter
        fun init() {
            formatter = DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .append(DateTimeFormatter.ISO_LOCAL_DATE)
                    .appendLiteral('T')
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .appendLiteral(':')
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .optionalStart()
                    .appendLiteral(':')
                    .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                    .appendLiteral('Z')
                    .toFormatter()
        }
    }
}