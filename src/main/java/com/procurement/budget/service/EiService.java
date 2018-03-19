package com.procurement.budget.service;

import com.procurement.budget.model.dto.bpe.ResponseDto;
import com.procurement.budget.model.dto.ei.EiDto;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public interface EiService {

    ResponseDto createEi(LocalDateTime startDate,
                         String country,
                         String owner,
                         EiDto eiDto);

    ResponseDto updateEi(String owner,
                         String cpId,
                         String token,
                         EiDto eiDto);

    EiDto getEi(String cpId);
}
