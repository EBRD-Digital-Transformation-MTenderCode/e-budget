package com.procurement.budget.controller

import com.procurement.budget.model.dto.ei.EiDto
import com.procurement.budget.service.EiService
import com.procurement.budget.model.bpe.ResponseDto
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/ei")
class EiController(private val eiService: EiService) {

    @PostMapping
    fun createEi(@RequestParam("owner") owner: String,
                 @RequestParam("country") country: String,
                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                 @RequestParam("date") dateTime: LocalDateTime,
                 @Valid @RequestBody data: EiDto): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity(eiService.createEi(owner, country, dateTime, data), HttpStatus.CREATED)
    }

    @PutMapping
    fun updateEi(@RequestParam("identifier") cpId: String,
                 @RequestParam("owner") owner: String,
                 @RequestParam("token") token: String,
                 @Valid @RequestBody data: EiDto): ResponseEntity<ResponseDto<*>> {
        return ResponseEntity(eiService.updateEi(owner, cpId, token, data), HttpStatus.OK)
    }
}
