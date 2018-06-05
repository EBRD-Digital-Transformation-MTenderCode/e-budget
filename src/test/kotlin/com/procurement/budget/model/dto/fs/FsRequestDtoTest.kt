package com.procurement.budget.model.dto.fs

import com.procurement.budget.model.dto.fs.create.FsCreateDto
import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsRequestDtoTest {

    @Test
    @DisplayName("fsRequestDtoRequired")
    fun fsRequestDtoRequired() {
        compare(FsCreateDto::class.java, "/json/fs_request_without_buyer.json")
    }

    @Test
    @DisplayName("fsRequestDtoFull")
    fun fsRequestDtoFull() {
        compare(FsCreateDto::class.java, "/json/fs_request_with_buyer.json")
    }
}