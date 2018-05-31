package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.testDeserialize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckRequestDtoTest {

    @Test
    @DisplayName("checkRequestDtoRequired")
    fun checkRequestDtoWithoutReq() {
        testDeserialize(CheckRequestDto::class.java, "/json/check_fs_request.json")
    }

    @Test
    @DisplayName("checkRequestDtoFull")
    fun checkRequestDtoFull() {
        testDeserialize(CheckRequestDto::class.java, "/json/check_fs_request_full.json")
    }
}
