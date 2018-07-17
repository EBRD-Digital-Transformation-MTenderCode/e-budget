package com.procurement.budget.controller

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.CheckRq
import com.procurement.budget.model.dto.fs.request.FsCreate
import com.procurement.budget.model.dto.fs.request.FsUpdate
import com.procurement.budget.service.CheckFsService
import com.procurement.budget.service.FsService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/fs")
class FsController(private val fsService: FsService,
                   private val checkFsService: CheckFsService) {

    @PostMapping
    fun createFs(@RequestParam("cpid") cpId: String,
                 @RequestParam("owner") owner: String,
                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                 @RequestParam("date") dateTime: LocalDateTime,
                 @Valid @RequestBody data: FsCreate): ResponseEntity<ResponseDto> {
        return ResponseEntity(fsService.createFs(
                cpId = cpId,
                owner = owner,
                dateTime = dateTime,
                fsDto = data), HttpStatus.CREATED)
    }

    @PutMapping
    fun updateFs(@RequestParam("cpid") cpId: String,
                 @RequestParam("ocid") ocId: String,
                 @RequestParam("token") token: String,
                 @RequestParam("owner") owner: String,
                 @Valid @RequestBody data: FsUpdate): ResponseEntity<ResponseDto> {
        return ResponseEntity(fsService.updateFs(
                cpId = cpId,
                ocId = ocId,
                token = token,
                owner = owner,
                fsDto = data), HttpStatus.OK)
    }

    @PostMapping("/check")
    fun checkFs(@Valid @RequestBody data: CheckRq): ResponseEntity<ResponseDto> {
        return ResponseEntity(checkFsService.checkFs(data), HttpStatus.OK)
    }
}
