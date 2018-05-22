package com.procurement.budget.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.budget.config.OCDSProperties
import com.procurement.budget.dao.EiDao
import com.procurement.budget.dao.FsDao
import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.model.dto.fs.FsDto
import com.procurement.budget.model.dto.fs.FsRequestDto
import com.procurement.budget.model.dto.fs.FsResponseDto
import com.procurement.budget.model.entity.EiEntity
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

class FsServiceImplTest {
    companion object {

        private val CPID = "ocds-t1s2t3-TEST-1526570694407"
        private val OCID = "ocds-t1s2t3-TEST-1526570694407-FS-1526570698032"
        private val OCID_SUFFICS = 1526570698032
        private val TOKEN = "90d6581a-c710-4f08-936d-e13fecd8c560"
        private val OWNER = "owner"
        private val AMOUNT = 123456789.98
        private val AMOUNT_RESERVED = null
        private val FS_JSON_REQUEST = "/json/fs_request.json"
        private val FS_JSON_RESPONSE = "/json/fs_response.json"
        private val FS_JSON = "/json/fs.json"
        private val EI_JSON_CREATE = "/json/ei.json"
        private val DATE = localNowUTC()

    }

    private lateinit var properties: OCDSProperties
    private lateinit var fsDao: FsDao
    private lateinit var eiService: EiServiceImpl
    private lateinit var fsService: FsServiceImpl
    private lateinit var generateService:GenerateServiceImpl
    private lateinit var fsEntity: FsEntity
    private lateinit var eiDto:EiDto
    private lateinit var fsDto:FsDto

    @BeforeEach
    fun init() {
        properties = mock()
        fsDao = mock()
        eiService = mock()
        generateService = mock()
        fsService = FsServiceImpl(fsDao, eiService,generateService)
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
            jsonData = getJsonFromFile(FS_JSON_REQUEST)
        )
    }

    @Test
    @DisplayName("createFs")
    fun createFs() {
        whenever(eiService.getEi(CPID)).thenReturn(eiDto)
        whenever(fsDao.getTotalAmountByCpId(CPID)).thenReturn(AMOUNT.toBigDecimal())
        whenever(generateService.generateRandomUUID()).thenReturn(UUID.fromString("90d6581a-c710-4f08-936d-e13fecd8c560"))
        whenever(generateService.getNowUtc()).thenReturn(1526570698032)
        val fsRequestDto = toObject(FsRequestDto::class.java, getJsonFromFile(FS_JSON_REQUEST))

        val fsResponseDto = toObject(FsResponseDto::class.java, getJsonFromFile(FS_JSON_RESPONSE))

        val response = fsService.createFs(CPID,OWNER, DATE,fsRequestDto)

        Assertions.assertEquals(ResponseDto(true, null, fsResponseDto), response)
    }


}