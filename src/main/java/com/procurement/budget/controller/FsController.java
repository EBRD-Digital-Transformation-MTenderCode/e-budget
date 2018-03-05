package com.procurement.budget.controller;

import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.fs.FsDto;
import com.procurement.budget.service.FsService;
import javax.validation.Valid;
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
    public ResponseEntity<ResponseDto> create(final String cpId,
                                              final String owner,
                                              @Valid @RequestBody final FsDto fsDto) {
        return new ResponseEntity<>(fsService.createFs(cpId, owner, fsDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> update(final String cpId,
                                              final String ocId,
                                              final String token,
                                              final String owner,
                                              @Valid @RequestBody final FsDto fsDto) {
        return new ResponseEntity<>(fsService.updateFs(cpId, ocId, token, owner, fsDto), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<ResponseDto> check(final String cpId,
//                                              final String ocId,
//                                              final String token,
//                                              final String owner,
//                                              @Valid @RequestBody final FsDto fsDto) {
//        return new ResponseEntity<>(fsService.checkFs(cpId, ocId, token, owner, fsDto), HttpStatus.OK);
//    }

}
