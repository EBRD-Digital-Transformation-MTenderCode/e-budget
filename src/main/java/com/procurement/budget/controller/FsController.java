package com.procurement.budget.controller;

import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.fs.FsDto;
import com.procurement.budget.model.dto.fs.FsRequestDto;
import com.procurement.budget.service.FsService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/fs")
public class FsController {

    private final FsService fsService;

    public FsController(final FsService fsService) {
        this.fsService = fsService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createFs(@RequestParam("identifier") final String cpId,
                                                @RequestParam("owner") final String owner,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                @RequestParam("date") final LocalDateTime dateTime,
                                                @Valid @RequestBody final FsRequestDto data) {
        return new ResponseEntity<>(fsService.createFs(cpId, owner, dateTime, data), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateFs(@RequestParam("identifier") final String cpId,
                                                @RequestParam("token") final String token,
                                                @RequestParam("owner") final String owner,
                                                @Valid @RequestBody final FsDto data) {
        return new ResponseEntity<>(fsService.updateFs(cpId, token, owner, data), HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<ResponseDto> checkFs(@Valid @RequestBody final CheckRequestDto data) {
        return new ResponseEntity<>(fsService.checkFs(data), HttpStatus.OK);
    }

}
