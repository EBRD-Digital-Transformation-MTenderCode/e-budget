package com.procurement.budget.service

import com.procurement.budget.dao.HistoryDao
import com.procurement.budget.model.dto.bpe.CommandMessage
import com.procurement.budget.model.dto.bpe.CommandType
import com.procurement.budget.model.dto.bpe.ResponseDto
import com.procurement.budget.utils.toObject
import org.springframework.stereotype.Service

interface CommandService {

    fun execute(cm: CommandMessage): ResponseDto

}

@Service
class CommandServiceImpl(private val historyDao: HistoryDao,
                         private val checkFsService: CheckFsService,
                         private val eiService: EiService,
                         private val fsService: FsService) : CommandService {


    override fun execute(cm: CommandMessage): ResponseDto {
        var historyEntity = historyDao.getHistory(cm.id, cm.command.value())
        if (historyEntity != null) {
            return toObject(ResponseDto::class.java, historyEntity.jsonData)
        }
        val response = when (cm.command) {
            CommandType.CHECK_FS -> checkFsService.checkFs(cm)
            CommandType.CREATE_EI -> eiService.createEi(cm)
            CommandType.UPDATE_EI -> eiService.updateEi(cm)
            CommandType.CREATE_FS -> fsService.createFs(cm)
            CommandType.UPDATE_FS -> fsService.updateFs(cm)
        }
        historyEntity = historyDao.saveHistory(cm.id, cm.command.value(), response)
        return toObject(ResponseDto::class.java, historyEntity.jsonData)
    }
}