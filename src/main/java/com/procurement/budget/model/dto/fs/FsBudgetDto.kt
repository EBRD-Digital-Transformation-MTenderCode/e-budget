package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "description", "period", "amount", "europeanUnionFunding", "isEuropeanUnionFunded", "verified", "sourceEntity", "verificationDetails")
data class FsBudgetDto(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @Valid
        @JsonProperty("period")
        val period: Period,

        @Valid
        @JsonProperty("amount")
        val amount: FsValue,

        @Valid
        @JsonProperty("europeanUnionFunding")
        val europeanUnionFunding: EuropeanUnionFunding?,

        @JsonProperty("isEuropeanUnionFunded")
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean,

        @JsonProperty("verified")
        @get:JsonProperty("verified")
        var verified: Boolean?,

        @Valid
        @JsonProperty("sourceEntity")
        var sourceEntity: FsOrganizationReferenceDto?,

        @JsonProperty("verificationDetails")
        val verificationDetails: String?
)
