package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckRequestDtoTest {

    @Test
    @DisplayName("checkRequestDto")
    fun checkRequestDto() {
        compare(CheckRequestDto::class.java, "/json/check_fs_request.json")
    }

    @Test
    @DisplayName("checkRequestDtoWithoutReq")
    fun checkRequestDtoWithoutReq() {
        compare(CheckRequestDto::class.java, "/json/check_fs_request_without_req.json")
    }

    @Test
    @DisplayName("checkRequestDtoFull")
    fun checkRequestDtoFull() {
        compare(CheckRequestDto::class.java, "/json/check_fs_request_full.json")
    }
}
