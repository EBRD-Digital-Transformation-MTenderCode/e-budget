package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details(

        @Valid
        @NotNull
        @JsonProperty("typeOfBuyer")
        val typeOfBuyer: TypeOfBuyer,

        @Valid
        @NotNull
        @JsonProperty("mainGeneralActivity")
        val mainGeneralActivity: MainGeneralActivity,

        @Valid
        @NotNull
        @JsonProperty("mainSectoralActivity")
        val mainSectoralActivity: MainSectoralActivity,

        @JsonProperty("isACentralPurchasingBody")
        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        @JsonProperty("NUTSCode")
        val nutsCode: String?,

        @Valid
        @NotNull
        @JsonProperty("scale")
        val scale: Scale
)
