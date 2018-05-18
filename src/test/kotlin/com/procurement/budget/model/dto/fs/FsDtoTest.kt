package com.procurement.budget.model.dto.fs

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsDtoTest {
    @Test
    @DisplayName("FsDto")
    fun fsDto() {
        compare(FsDto::class.java, "/json/fs.json")
    }
}