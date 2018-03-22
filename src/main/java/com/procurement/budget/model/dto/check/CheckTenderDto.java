package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.Classification;
import com.procurement.budget.model.dto.ocds.Period;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckTenderDto {

    @JsonProperty("tenderPeriod")
    private final Period tenderPeriod;

//    @JsonProperty("classification")
//    @Valid
//    private final Classification classification;

    @JsonCreator
    public CheckTenderDto(@JsonProperty("tenderPeriod") final Period tenderPeriod) {
        this.tenderPeriod = tenderPeriod;
    }
}
