package com.procurement.budget.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.budget.utils.milliNowUTC
import org.springframework.stereotype.Service
import java.util.*

interface GenerateService {

    fun generateRandomUUID(): UUID

    fun getNowUtc(): Long
}

@Service
class GenerateServiceImpl : GenerateService {

    override fun generateRandomUUID(): UUID {
        return UUIDs.random()
    }

    override fun getNowUtc(): Long {
        return milliNowUTC()
    }
}