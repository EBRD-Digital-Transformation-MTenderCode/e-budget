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
}