package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details(

        @field:Valid
        @field:NotNull
        @JsonProperty("typeOfBuyer")
        val typeOfBuyer: TypeOfBuyer,

        @field:Valid
        @field:NotNull
        @JsonProperty("mainGeneralActivity")
        val mainGeneralActivity: MainGeneralActivity,

        @field:Valid
        @field:NotNull
        @JsonProperty("mainSectoralActivity")
        val mainSectoralActivity: MainSectoralActivity,

        @field:NotNull
        @JsonProperty("isACentralPurchasingBody")
        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean,

        @JsonProperty("NUTSCode")
        val nutsCode: String?,

        @field:Valid
        @field:NotNull
        @JsonProperty("scale")
        val scale: Scale
)
