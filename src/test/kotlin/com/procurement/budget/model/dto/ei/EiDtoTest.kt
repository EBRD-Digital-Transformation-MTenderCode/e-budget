package com.procurement.budget.model.dto.ei

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class EiDtoTest {

    @Test
    @DisplayName("EiDtoRequired")
    fun eiDtoWithout() {
        compare(EiDto::class.java, "/json/ei_request.json")
    }

    @Test
    @DisplayName("EiDtoFull")
    fun eiDtoFull() {
        compare(EiDto::class.java, "/json/ei_request_full.json")
    }
}