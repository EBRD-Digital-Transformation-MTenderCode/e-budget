package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.budget.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.budget.model.dto.fs.FsPlanningDto;
import com.procurement.budget.model.dto.fs.FsTenderDto;
import com.procurement.budget.model.dto.ocds.Organization;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonPropertyOrder({
        "planning",
        "tender"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckRequestDto {

    @JsonProperty("planning")
    private final CheckPlanningDto planning;
    @JsonProperty("tender")
    private CheckTenderDto tender;

    @JsonCreator
    public CheckRequestDto(@JsonProperty("planning") final CheckPlanningDto planning,
                           @JsonProperty("tender") final CheckTenderDto tender) {
        this.planning = planning;
        this.tender = tender;
    }
}
