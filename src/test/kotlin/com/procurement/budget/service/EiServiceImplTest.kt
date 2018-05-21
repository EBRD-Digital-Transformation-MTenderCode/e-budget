package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.getJsonFromFile
import com.procurement.budget.utils.localNowUTC
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toObject
import com.procurement.notice.model.bpe.ResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*


class EiServiceImplTest {

    companion object {
        private val CPID = "ocds-t1s2t3-TEST-1526570694407"
        private val TOKEN = "90d6581a-c710-4f08-936d-e13fecd8c560"
        private val OWNER = "owner"
        private val COUNTRY = "TEST"
        private val FILE_JSON = "/json/ei.json"
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
                token = UUID.fromString(TOKEN),
                owner = OWNER,
                createdDate = localNowUTC().toDate(),
                jsonData = getJsonFromFile(FILE_JSON))
    }

    @Test
    @DisplayName("createEi")
    fun createEi() {
        whenever(service.getCpId(COUNTRY)).thenReturn(CPID)
        val eiDto = toObject(EiDto::class.java, getJsonFromFile(FILE_JSON))
        val response = service.createEi(OWNER, COUNTRY, localNowUTC(), eiDto)
        assertEquals(ResponseDto(true, null, eiDto), response)
    }

    @Test
    @DisplayName("updateEi")
    fun updateEi() {
        whenever(eiDao.getByCpId(CPID)).thenReturn(eiEntity)
        val eiDto = toObject(EiDto::class.java, getJsonFromFile(FILE_JSON))
        val response = service.updateEi(OWNER, CPID, TOKEN, eiDto)
        assertEquals(ResponseDto(true, null, eiDto), response)
    }

    @Test
    @DisplayName("getEi")
    fun getEi() {
        whenever(eiDao.getByCpId(CPID)).thenReturn(eiEntity)
        val ei = service.getEi(CPID)
        assert(ei.ocid == CPID)
    }
}