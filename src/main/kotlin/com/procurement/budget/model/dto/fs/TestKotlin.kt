package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TestKotlin(

        @JsonProperty("id")
        val id: String?,

        @field:NotNull
        @get:JsonProperty("isEuropeanUnionFunded")
        @JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?

)
