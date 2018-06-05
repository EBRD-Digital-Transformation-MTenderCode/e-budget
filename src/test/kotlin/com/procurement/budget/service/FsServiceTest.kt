package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.FsDao
import com.procurement.budget.model.dto.check.CheckRequestDto
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.ocds.Fs
import com.procurement.budget.model.dto.fs.create.FsCreateDto
import com.procurement.budget.model.entity.FsEntity
import com.procurement.budget.utils.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class FsServiceTest {
    companion object {

        private const val CPID = "ocds-t1s2t3-TEST-1526570694407"
        private const val OCID = "ocds-t1s2t3-TEST-1526570694407-FS-1526570698032"
        private const val OCID_TIME_STAMP = 1526570698032
        private const val TOKEN = "90d6581a-c710-4f08-936d-e13fecd8c560"
        private const val OWNER = "owner"
        private val AMOUNT = 123456789.98
        private val AMOUNT_RESERVED = null
        private const val FS_JSON_REQUEST_WITHOUT_BUYER = "/json/fs_request_without_buyer.json"
        private const val FS_JSON_RESPONSE_WITHOUT_BUYER = "/json/fs_response_without_buyer.json"
        private const val FS_JSON_REQUEST_WITH_BUYER = "/json/fs_request_with_buyer.json"
        private const val FS_JSON_RESPONSE_WITH_BUYER = "/json/fs_response_with_buyer.json"
        private const val CHECK_FS_JSON_REQUEST = "/json/check_fs_request.json"
        private const val CHECK_FS_JSON_RESPONSE = "/json/check_fs_response.json"
        private const val FS_JSON = "/json/fs.json"
        private const val EI_JSON_CREATE = "/json/ei.json"
        private val DATE = localNowUTC()
    }

    private lateinit var properties: OCDSProperties
    private lateinit var fsDao: FsDao
    private lateinit var eiService: EiServiceImpl
    private lateinit var fsService: FsServiceImpl
    private lateinit var generationService: GenerationServiceImpl
    private lateinit var fsEntity: FsEntity
    private lateinit var eiDto: EiDto
    private lateinit var fsDto: Fs
    private lateinit var fsEntities: List<FsEntity>
    private lateinit var cpIds: Set<String>

    @BeforeEach
    fun init() {
        properties = mock()
        fsDao = mock()
        eiService = mock()
        generationService = mock()
        fsService = FsServiceImpl(fsDao, eiService, generationService)
        eiDto = toObject(EiDto::class.java, getJsonFromFile(EI_JSON_CREATE))
        fsDto = toObject(Fs::class.java, getJsonFromFile(FS_JSON))
        fsEntity = FsEntity(
                cpId = CPID,
                ocId = OCID,
                token = UUID.fromString(TOKEN),
                owner = OWNER,
                amount = AMOUNT.toBigDecimal(),
                amountReserved = AMOUNT_RESERVED,
                createdDate = DATE.toDate(),
                jsonData = getJsonFromFile(FS_JSON)
        )
        cpIds = setOf(CPID)
        fsEntities = listOf(fsEntity)
    }

    @Test
    @DisplayName("createFsWithoutBuyer")
    fun createFsWithoutBuyer() {
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        whenever(fsDao.getTotalAmountByCpId(CPID)).thenReturn(AMOUNT.toBigDecimal())
        whenever(generationService.generateRandomUUID()).thenReturn(UUID.fromString(TOKEN))
        whenever(generationService.getNowUtc()).thenReturn(OCID_TIME_STAMP)
        val request = toObject(FsCreateDto::class.java, getJsonFromFile(FS_JSON_REQUEST_WITHOUT_BUYER))
        val response = fsService.createFs(CPID, OWNER, DATE, request)
        val responseExpected = getJsonFromFile(FS_JSON_RESPONSE_WITHOUT_BUYER)
        val responseSerialised = toJson(response.data)
        Assertions.assertEquals(responseExpected, responseSerialised)
    }

    @Test
    @DisplayName("createFsWithBuyer")
    fun createFsWithBuyer() {
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        whenever(fsDao.getTotalAmountByCpId(CPID)).thenReturn(AMOUNT.toBigDecimal())
        whenever(generationService.generateRandomUUID()).thenReturn(UUID.fromString(TOKEN))
        whenever(generationService.getNowUtc()).thenReturn(OCID_TIME_STAMP)
        val request = toObject(FsCreateDto::class.java, getJsonFromFile(FS_JSON_REQUEST_WITH_BUYER))
        val response = fsService.createFs(CPID, OWNER, DATE, request)
        val responseExpected = getJsonFromFile(FS_JSON_RESPONSE_WITH_BUYER)
        val responseSerialised = toJson(response.data)
        Assertions.assertEquals(responseExpected, responseSerialised)
    }

    @Test
    @DisplayName("checkFs")
    fun checkFs() {
        whenever(fsDao.getAllByCpIds(cpIds)).thenReturn(fsEntities)
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        val request = toObject(CheckRequestDto::class.java, getJsonFromFile(CHECK_FS_JSON_REQUEST))
        val response = fsService.checkFs(request)
        val responseExpected = getJsonFromFile(CHECK_FS_JSON_RESPONSE)
        val responseSerialised = toJson(response.data)
        Assertions.assertEquals(responseExpected, responseSerialised)
    }


}