package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckResponseDtoTest {

    @Test
    @DisplayName("CheckResponseDto")
    fun checkResponseDto() {
        compare(CheckResponseDto::class.java, "/json/check_fs_response.json")
    }
}
