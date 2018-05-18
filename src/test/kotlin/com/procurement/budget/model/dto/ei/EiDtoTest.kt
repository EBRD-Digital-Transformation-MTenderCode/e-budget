package com.procurement.budget.model.dto.ei

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EiDtoTest {
    @Test
    @DisplayName("EiDto")
    fun eiDto() {
        compare(EiDto::class.java, "/json/ei.json")
    }
}