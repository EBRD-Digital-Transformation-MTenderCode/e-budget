package com.procurement.budget.controller

import com.procurement.budget.exception.EnumException
import com.procurement.budget.exception.ErrorException
import com.procurement.budget.model.dto.bpe.*
import com.procurement.budget.service.CheckFsService
import com.procurement.budget.service.EiService
import com.procurement.budget.service.FsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val checkFsService: CheckFsService,
                        private val eiService: EiService,
                        private val fsService: FsService) {

    @PostMapping
    fun command(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(execute(commandMessage), HttpStatus.OK)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.CHECK_FS -> checkFsService.checkFs(cm)
            CommandType.CREATE_EI -> eiService.createEi(cm)
            CommandType.UPDATE_EI -> eiService.updateEi(cm)
            CommandType.CREATE_FS -> fsService.createFs(cm)
            CommandType.UPDATE_FS -> fsService.updateFs(cm)
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return when (ex) {
            is ErrorException -> getErrorExceptionResponseDto(ex)
            is EnumException -> getEnumExceptionResponseDto(ex)
            else -> getExceptionResponseDto(ex)
        }
    }
}



