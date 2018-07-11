package com.procurement.budget.model.dto.bpe

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.budget.exception.ErrorException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        val errors: List<ResponseErrorDto>?,

        val data: Any?,

        val id: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(

        val code: String,

        val description: String?
)

fun getResponseDto(data: Any?, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = null,
            data = data,
            id = id)
}

fun getExceptionResponseDto(exception: Exception, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.10.00",
                    description = exception.message
            )),
            data = null,
            id = id)
}

fun getErrorResponseDto(error: ErrorException, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.10." + error.code,
                    description = error.msg
            )),
            data = null,
            id = id)
}