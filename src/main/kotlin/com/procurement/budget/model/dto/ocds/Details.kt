package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        @field:NotNull
        val typeOfBuyer: TypeOfBuyer,

        @field:NotNull
        val mainGeneralActivity: MainGeneralActivity,

        @field:NotNull
        val mainSectoralActivity: MainSectoralActivity

)
