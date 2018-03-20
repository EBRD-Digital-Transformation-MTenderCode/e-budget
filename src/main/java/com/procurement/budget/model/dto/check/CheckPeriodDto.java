package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.budget.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.budget.model.dto.ocds.Value;
import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckPeriodDto {
    @JsonProperty("startDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull
    private final LocalDateTime startDate;

    @JsonCreator
    public CheckPeriodDto(@JsonProperty("startDate") final LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
