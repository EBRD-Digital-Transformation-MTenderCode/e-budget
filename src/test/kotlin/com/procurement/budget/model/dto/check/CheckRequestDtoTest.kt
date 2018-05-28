package com.procurement.budget.model.dto.check

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CheckRequestDtoTest {

    @Test
    @DisplayName("checkRequestDtoRequired")
    fun checkRequestDtoWithoutReq() {
        compare(CheckRequestDto::class.java, "/json/check_fs_request_only_req.json")
    }

    @Test
    @DisplayName("checkRequestDtoFull")
    fun checkRequestDtoFull() {
        compare(CheckRequestDto::class.java, "/json/check_fs_request_full.json")
    }
}
