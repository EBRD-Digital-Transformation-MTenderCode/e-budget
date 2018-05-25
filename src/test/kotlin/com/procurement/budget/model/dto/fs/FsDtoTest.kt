package com.procurement.budget.model.dto.fs

import com.procurement.budget.utils.compare
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FsDtoTest {
    @Test
    @DisplayName("FstDto")
    fun fsDto() {
        compare(FsDto::class.java, "/json/fs.json")
    }

    @Test
    @DisplayName("FsDtoWithoutReq")
    fun fsDtoWithoutReq() {
        compare(FsDto::class.java, "/json/fs_without_req.json")
    }
    @Test
    @DisplayName("FsDtoFull")
    fun fsDtoFull() {
        compare(FsDto::class.java, "/json/fs_full.json")
    }


}