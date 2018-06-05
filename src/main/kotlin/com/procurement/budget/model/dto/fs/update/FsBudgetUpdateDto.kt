package com.procurement.budget.model.dto.fs.update

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto
import com.procurement.budget.model.dto.fs.FsValue
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding
import com.procurement.budget.model.dto.ocds.Period
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsBudgetUpdateDto(

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

        @NotNull
        @JsonProperty("verified")
        @get:JsonProperty("verified")
        var verified: Boolean,

        @Valid
        @NotNull
        @JsonProperty("sourceEntity")
        var sourceEntity: FsOrganizationReferenceDto?
)
