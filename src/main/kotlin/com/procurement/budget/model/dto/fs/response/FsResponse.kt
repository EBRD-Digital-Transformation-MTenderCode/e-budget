package com.procurement.budget.model.dto.fs.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.budget.model.dto.fs.Fs

data class FsResponse @JsonCreator constructor(

        val ei: EiForFs?,

        val fs: Fs
)
