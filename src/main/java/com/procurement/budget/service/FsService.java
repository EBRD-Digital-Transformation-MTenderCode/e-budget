package com.procurement.budget.service;

import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.fs.FsDto;
import com.procurement.budget.model.dto.fs.FsRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface FsService {

    ResponseDto createFs(String cpId,
                         String owner,
                         FsRequestDto fsDto);

    ResponseDto updateFs(String cpId,
                         String ocId,
                         String token,
                         String owner,
                         FsDto fsDto);
}
