package com.procurement.budget.model.dto.fs.create

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsValue
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsBudgetCreateDto(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @Valid
        @NotNull
        @JsonProperty("period")
        val period: Period,

        @Valid
        @NotNull
        @JsonProperty("amount")
        val amount: FsValue,

        @NotNull
        @JsonProperty("isEuropeanUnionFunded")
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean,

        @Valid
        @JsonProperty("europeanUnionFunding")
        val europeanUnionFunding: EuropeanUnionFunding?,

        @JsonProperty("verified")
        @get:JsonProperty("verified")
        var verified: Boolean?,

        @Valid
        @JsonProperty("sourceEntity")
        var sourceEntity: FsOrganizationReferenceDto?
)
