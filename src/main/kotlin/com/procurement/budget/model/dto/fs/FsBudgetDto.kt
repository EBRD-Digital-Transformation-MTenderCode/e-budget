package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsBudgetDto @JsonCreator constructor(

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("description")
        var description: String?,

        @Valid
        @NotNull
        @JsonProperty("period")
        val period: Period,

        @Valid
        @NotNull
        @JsonProperty("amount")
        val amount: FsValue,

        @Valid
        @JsonProperty("europeanUnionFunding")
        var europeanUnionFunding: EuropeanUnionFunding?,

        @NotNull
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
        var verificationDetails: String?
)
