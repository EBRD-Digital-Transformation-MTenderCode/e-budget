package com.procurement.budget.controller

import com.procurement.budget.model.bpe.ResponseDto
import com.procurement.budget.model.dto.check.CheckRequestDto
import com.procurement.budget.model.dto.fs.FsRequestCreateDto
import com.procurement.budget.model.dto.fs.FsRequestUpdateDto
import com.procurement.budget.service.FsService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/fs")
class FsController(private val fsService: FsService) {

    @PostMapping
    fun createFs(@RequestParam("identifier") cpId: String,
                 @RequestParam("owner") owner: String,
                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                 @RequestParam("date") dateTime: LocalDateTime,
                 @Valid @RequestBody data: FsRequestCreateDto): ResponseEntity<ResponseDto> {
        return ResponseEntity(fsService.createFs(cpId, owner, dateTime, data), HttpStatus.CREATED)
    }

    @PutMapping
    fun updateFs(@RequestParam("identifier") cpId: String,
                 @RequestParam("token") token: String,
                 @RequestParam("owner") owner: String,
                 @Valid @RequestBody data: FsRequestUpdateDto): ResponseEntity<ResponseDto> {
        return ResponseEntity(fsService.updateFs(cpId, token, owner, data), HttpStatus.OK)
    }

    @PostMapping("/check")
    fun checkFs(@Valid @RequestBody data: CheckRequestDto): ResponseEntity<ResponseDto> {
        return ResponseEntity(fsService.checkFs(data), HttpStatus.OK)
    }
}
