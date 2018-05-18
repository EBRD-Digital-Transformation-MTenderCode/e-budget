package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.TestUtils
import com.procurement.budget.utils.localNowUTC
import com.procurement.budget.utils.toDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*


class EiServiceImplTest {

    companion object {
        private val CPID = "cpid"
    }

    private lateinit var properties: OCDSProperties
    private lateinit var eiDao: EiDao
    private lateinit var service: EiServiceImpl
    private lateinit var eiEntity: EiEntity

    @BeforeEach
    fun init() {
        properties = mock()
        eiDao = mock()
        service = EiServiceImpl(properties, eiDao)
        eiEntity = EiEntity(
                cpId = CPID,
                token = UUID.randomUUID(),
                owner = "owner",
                createdDate = localNowUTC().toDate(),
                jsonData = TestUtils().getJsonFromFile("/json/ei.json"))
    }

    @Test
    @DisplayName("createEi")
    fun createEi() {
    }

    @Test
    @DisplayName("updateEi")
    fun updateEi() {
    }

    @Test
    @DisplayName("getEi")
    fun getEi() {
        whenever(eiDao.getByCpId(CPID)).thenReturn(eiEntity)
        val ei = service.getEi(CPID)
        assert(ei.ocId == CPID)
    }
}