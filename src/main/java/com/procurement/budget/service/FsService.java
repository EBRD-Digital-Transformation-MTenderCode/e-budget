package com.procurement.budget.service;

import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.check.CheckRequestDto;
import com.procurement.budget.model.dto.fs.FsDto;
import com.procurement.budget.model.dto.fs.FsRequestDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface FsService {

    ResponseDto createFs(String cpId,
                         String owner,
                         LocalDateTime dateTime,
                         FsRequestDto fsDto);

    ResponseDto updateFs(String cpId,
                         String ocId,
                         String token,
                         String owner,
                         FsDto fsDto);

    ResponseDto checkFs(CheckRequestDto dto);
}
