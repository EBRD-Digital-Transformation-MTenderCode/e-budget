package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckRequestDtoTest {

    @Test
    @DisplayName("checkRequestDto")
    fun checkRequestDto() {
        compare(CheckRequestDto::class.java, "/json/checkFs.json")
    }
}
