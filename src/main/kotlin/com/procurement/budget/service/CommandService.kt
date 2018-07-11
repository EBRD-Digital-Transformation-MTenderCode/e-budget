//package com.procurement.budget.service
//
//import com.procurement.budget.model.dto.bpe.CommandMessage
//import com.procurement.budget.model.dto.bpe.ResponseDto
//import org.springframework.stereotype.Service
//
//interface CommandService {
//
//    fun execute(cm: CommandMessage): ResponseDto
//
//}
//
//@Service
//class CommandServiceImpl(private val tenderDataService: TenderDataService) : CommandService {
//
//    override fun execute(cm: CommandMessage): ResponseDto {
//        return when (cm.command) {
//            CommandType.TENDER_CPV -> tenderDataService.tenderCPV(cm)
//            CommandType.CHECK_CURRENCY -> tenderDataService.tenderCPV(cm)
//        }
//    }
//}
