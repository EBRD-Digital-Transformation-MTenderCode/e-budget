package com.procurement.budget.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.budget.utils.milliNowUTC
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenerationService {

    fun generateRandomUUID(): UUID {
        return UUIDs.random()
    }

    fun getNowUtc(): Long {
        return milliNowUTC()
    }

    fun generateTenderId(): UUID {
        return UUIDs.random()
    }

    fun generateItemId(): UUID {
        return UUIDs.random()
    }
}