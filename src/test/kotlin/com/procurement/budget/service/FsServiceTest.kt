package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.FsDao
import com.procurement.budget.model.dto.check.CheckRequestDto
import com.procurement.budget.model.dto.check.CheckResponseDto
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.fs.FsDto
import com.procurement.budget.model.dto.fs.FsRequestDto
import com.procurement.budget.model.dto.fs.FsResponseDto
import com.procurement.budget.model.entity.FsEntity
import com.procurement.budget.utils.getJsonFromFile
import com.procurement.budget.utils.localNowUTC
import com.procurement.budget.utils.toDate
import com.procurement.budget.utils.toObject
import com.procurement.notice.model.bpe.ResponseDto
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
        private const val FS_JSON_REQUEST = "/json/fs_request.json"
        private const val FS_JSON_RESPONSE = "/json/fs_response.json"
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
    private lateinit var fsDto: FsDto
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
        fsDto = toObject(FsDto::class.java, getJsonFromFile(FS_JSON))
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
    @DisplayName("createFs")
    fun createFs() {
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        whenever(fsDao.getTotalAmountByCpId(CPID)).thenReturn(AMOUNT.toBigDecimal())
        whenever(generationService.generateRandomUUID()).thenReturn(UUID.fromString(TOKEN))
        whenever(generationService.getNowUtc()).thenReturn(OCID_TIME_STAMP)
        val fsRequestDto = toObject(FsRequestDto::class.java, getJsonFromFile(FS_JSON_REQUEST))
        val fsResponseDto = toObject(FsResponseDto::class.java, getJsonFromFile(FS_JSON_RESPONSE))
        val response = fsService.createFs(CPID, OWNER, DATE, fsRequestDto)
        Assertions.assertEquals(ResponseDto(true, null, fsResponseDto), response)
    }

    @Test
    @DisplayName("updateFs")
    fun updateFs() {
        whenever(fsDao.getByCpIdAndToken(CPID, UUID.fromString(TOKEN))).thenReturn(fsEntity)
        whenever(fsDao.getTotalAmountByCpId(CPID)).thenReturn(AMOUNT.toBigDecimal())
        val fsDto = toObject(FsDto::class.java, getJsonFromFile(FS_JSON))
        val fsResponseDto = toObject(FsResponseDto::class.java, getJsonFromFile(FS_JSON_RESPONSE))
        val response = fsService.updateFs(CPID, TOKEN, OWNER, fsDto)
        Assertions.assertEquals(ResponseDto(true, null, fsResponseDto), response)
    }

    @Test
    @DisplayName("checkFs")
    fun checkFs() {
        whenever(fsDao.getAllByCpIds(cpIds)).thenReturn(fsEntities)
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        val checkFsRequest = toObject(CheckRequestDto::class.java, getJsonFromFile(CHECK_FS_JSON_REQUEST))
        val checkFsResponse = toObject(CheckResponseDto::class.java, getJsonFromFile(CHECK_FS_JSON_RESPONSE))
        val response = fsService.checkFs(checkFsRequest)
        Assertions.assertEquals(ResponseDto(true, null, checkFsResponse), response)
    }


}