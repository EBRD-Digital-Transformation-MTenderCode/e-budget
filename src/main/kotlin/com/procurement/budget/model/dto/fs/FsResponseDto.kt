package com.procurement.budget.model.dto.fs

import com.fasterxml.jackson.annotation.JsonCreator

data class FsResponseDto @JsonCreator constructor(

        val ei: EiForFs?,

        val fs: Fs
)
