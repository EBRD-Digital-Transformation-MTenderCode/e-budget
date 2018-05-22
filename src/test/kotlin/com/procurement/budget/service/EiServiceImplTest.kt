package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.*
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
        private val createdDate = localNowUTC()
    }

    private lateinit var properties: OCDSProperties
    private lateinit var eiDao: EiDao
    private lateinit var service: EiServiceImpl
    private lateinit var eiEntity: EiEntity
    private lateinit var eiDto: EiDto

    @BeforeEach
    fun init() {
        properties = mock()
        eiDao = mock()
        service = EiServiceImpl(properties, eiDao)
        eiDto = toObject(EiDto::class.java, getJsonFromFile(FILE_JSON))
        eiEntity = EiEntity(
                cpId = CPID,
                token = UUID.fromString(TOKEN),
                owner = OWNER,
                createdDate = createdDate.toDate(),
                jsonData = toJson(eiDto))
    }

    @Test
    @DisplayName("createEi")
    fun createEi() {
        whenever(service.getCpId(COUNTRY)).thenReturn(CPID)
        val response = service.createEi(OWNER, COUNTRY, createdDate, eiDto.copy())
        val eiDtoFromResponse = response.data as EiDto
        assertEquals(eiDto, eiDtoFromResponse)
    }

    @Test
    @DisplayName("updateEi")
    fun updateEi() {
        whenever(eiDao.getByCpId(CPID)).thenReturn(eiEntity)
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