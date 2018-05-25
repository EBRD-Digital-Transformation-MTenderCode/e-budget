package com.procurement.budget.model.dto.fs

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsRequestDtoTest {

    @Test
    @DisplayName("FsRequestDto")
    fun fsRequestDto() {
        compare(FsRequestDto::class.java, "/json/fs_request.json")
    }

    @Test
    @DisplayName("FsRequestDtoWithoutReq")
    fun fsRequestDtoWithoutReq() {
        compare(FsRequestDto::class.java, "/json/fs_request_without_req.json")
    }

    @Test
    @DisplayName("FsRequestDtoFull")
    fun fsRequestDtoFull() {
        compare(FsRequestDto::class.java, "/json/fs_request_full.json")
    }
}