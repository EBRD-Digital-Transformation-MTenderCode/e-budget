package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.entity.EiEntity
import com.procurement.budget.utils.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*


class EiServiceTest {
//
//    companion object {
//        private val CPID = "ocds-t1s2t3-TEST-1526570698032"
//        private val TOKEN = "90d6581a-c710-4f08-936d-e13fecd8c560"
//        private val OCID_TIME_STAMP = 1526570698032
//        private val OWNER = "owner"
//        private val COUNTRY = "TEST"
//        private const val EI_JSON = "/json/ei.json"
//        private const val EI_CREATE_JSON_REQUEST = "/json/ei_request.json"
//        private const val EI_JSON_RESPONSE = "/json/ei_response.json"
//        private val createdDate = localNowUTC()
//    }
//
//    private lateinit var properties: OCDSProperties
//    private lateinit var eiDao: EiDao
//    private lateinit var fsDao: FsDao
//    private lateinit var service: EiServiceImpl
//    private lateinit var generationService: GenerationServiceImpl
//    private lateinit var eiEntity: EiEntity
//    private lateinit var ei: EiDto
//
//    @BeforeEach
//    fun init() {
//        properties = mock()
//        eiDao = mock()
//        fsDao = mock()
//        generationService = mock()
//        service = EiServiceImpl(properties, eiDao,fsDao, generationService)
//        ei = toObject(EiDto::class.java, getJsonFromFile(EI_JSON))
//        eiEntity = EiEntity(
//                cpId = CPID,
//                token = UUID.fromString(TOKEN),
//                owner = OWNER,
//                createdDate = createdDate.toDate(),
//                jsonData = toJson(ei))
//    }
//
//    @Test
//    @DisplayName("createEi")
//    fun createEi() {
//        whenever(properties.prefix).thenReturn("ocds-t1s2t3")
//        whenever(generationService.generateRandomUUID()).thenReturn(UUID.fromString(TOKEN))
//        whenever(generationService.getNowUtc()).thenReturn(OCID_TIME_STAMP)
//        val request = toObject(EiDto::class.java, getJsonFromFile(EI_CREATE_JSON_REQUEST))
//        val response = service.createEi(OWNER, COUNTRY, createdDate, request)
//        val responseExpected = getJsonFromFile(EI_JSON_RESPONSE)
//        val responseSerialised = toJson(response.data)
//        Assertions.assertEquals(responseExpected, responseSerialised)
//    }
//
//    @Test
//    @DisplayName("getEi")
//    fun getEi() {
//        whenever(eiDao.getByCpId(CPID)).thenReturn(eiEntity)
//        val result = service.getEi(CPID)
//        Assertions.assertEquals(ei, result)
//    }
}