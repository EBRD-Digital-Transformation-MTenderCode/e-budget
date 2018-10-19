package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: TypeOfBuyer?,

        val mainGeneralActivity: MainGeneralActivity?,

        val mainSectoralActivity: MainSectoralActivity?

)
