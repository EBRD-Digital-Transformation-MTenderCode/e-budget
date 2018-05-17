package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("typeOfBuyer", "mainGeneralActivity", "mainSectoralActivity", "isACentralPurchasingBody", "NUTSCode", "scale")
data class Details(

        @JsonProperty("typeOfBuyer")
        val typeOfBuyer: TypeOfBuyer,

        @JsonProperty("mainGeneralActivity")
        val mainGeneralActivity: MainGeneralActivity,

        @JsonProperty("mainSectoralActivity")
        val mainSectoralActivity: MainSectoralActivity,

        @JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        @JsonProperty("NUTSCode")
        val nutsCode: String?,

        @JsonProperty("scale")
        val scale: Scale
)
