package com.procurement.budget.controller;

import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.ei.EiDto;
import com.procurement.budget.service.EiService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/ei")
public class EiController {

    private final EiService eiService;

    public EiController(final EiService eiService) {
        this.eiService = eiService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createEi(@RequestParam final String owner,
                                                @RequestParam final String country,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                @RequestParam final LocalDateTime date,
                                                @Valid @RequestBody final EiDto eiDto) {
        return new ResponseEntity<>(eiService.createEi(owner, country, date, eiDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateEi(@RequestParam final String cpId,
                                                @RequestParam final String owner,
                                                @RequestParam final String token,
                                                @Valid @RequestBody final EiDto eiDto) {
        return new ResponseEntity<>(eiService.updateEi(owner, cpId, token, eiDto), HttpStatus.OK);
    }
}
