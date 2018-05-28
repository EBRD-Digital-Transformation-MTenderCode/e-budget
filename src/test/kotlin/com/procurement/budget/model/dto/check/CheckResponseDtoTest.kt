package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckResponseDtoTest {

    @Test
    @DisplayName("CheckResponseDtoRequired")
    fun checkResponseDtoWithoutReq() {
        compare(CheckResponseDto::class.java, "/json/check_fs_response_only_req.json")
    }

    @Test
    @DisplayName("CheckResponseDtoFull")
    fun checkResponseDtoFull() {
        compare(CheckResponseDto::class.java, "/json/check_fs_response_full.json")
    }
}
