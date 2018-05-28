package com.procurement.budget.model.dto.fs

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsResponseDtoTest {
    @Test
    @DisplayName("FsResponseDto")
    fun fsResponseDto() {
        compare(FsResponseDto::class.java, "/json/fs_response.json")
    }
    @Test
    @DisplayName("FsResponseDtoWithoutReq")
    fun fsResponseDtoWithoutReq() {
        compare(FsResponseDto::class.java, "/json/fs_response_without_req.json")
    }

    @Test
    @DisplayName("FsResponseDtoFull")
    fun fsResponseDtoFull() {
        compare(FsResponseDto::class.java, "/json/fs_response_full.json")
    }
}