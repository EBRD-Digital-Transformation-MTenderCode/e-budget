package com.procurement.budget.model.entity

import java.math.BigDecimal
import java.util.*

class FsEntity(

        var cpId: String,

        var ocId: String,

        var token: UUID,

        var owner: String,

        var amount: BigDecimal,

        var amountReserved: BigDecimal?,

        var createdDate: Date,

        var jsonData: String
)
