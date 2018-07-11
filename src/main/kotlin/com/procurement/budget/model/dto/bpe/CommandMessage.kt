package com.procurement.budget.model.dto.bpe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime
import java.util.*

data class CommandMessage @JsonCreator constructor(

        val id: String,

        val command: CommandType,

        val context: Context,

        val data: JsonNode?,

        val version: ApiVersion
)

data class Context @JsonCreator constructor(
        val startDate: LocalDateTime,
        val cpid: String,
        val token: String,
        val owner: String,
        val country: String,
        val language: String
)

enum class CommandType(private val value: String) {
    CREATE_EI("createEi"),
    UPDATE_EI("updateEi"),
    CREATE_FS("createFs"),
    UPDATE_FS("updateFs"),
    CHECK_FS("checkFs");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

enum class ApiVersion(private val value: String) {
    V_0_0_1("0.0.1");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}
