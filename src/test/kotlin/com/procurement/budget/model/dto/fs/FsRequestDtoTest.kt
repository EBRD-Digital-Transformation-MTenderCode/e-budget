package com.procurement.budget.model.dto.fs

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsRequestDtoTest {

    @Test
    @DisplayName("FsRequestDtoRequired")
    fun fsRequestDtoWithoutReq() {
        compare(FsRequestDto::class.java, "/json/fs_request_only_req.json")
    }

    @Test
    @DisplayName("FsRequestDtoFull")
    fun fsRequestDtoFull() {
        compare(FsRequestDto::class.java, "/json/fs_request_full.json")
    }
}