package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.budget.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.budget.model.dto.ocds.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "token",
        "ocid",
        "date",
        "tender",
        "planning",
        "parties"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EiResponseDto {
    @JsonProperty("token")
    private final String token;
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @JsonProperty("tender")
    private final EiTenderDto tender;
    @JsonProperty("planning")
    private final EiPlanningDto planning;
    @JsonProperty("parties")
    private final List<Organization> parties;

    @JsonCreator
    public EiResponseDto(@JsonProperty("token") final String token,
                         @JsonProperty("ocid") final String ocId,
                         @JsonProperty("date") final LocalDateTime date,
                         @JsonProperty("tender") final EiTenderDto tender,
                         @JsonProperty("planning") final EiPlanningDto planning,
                         @JsonProperty("parties") final List<Organization> parties) {
        this.token = token;
        this.ocId = ocId;
        this.date = date;
        this.tender = tender;
        this.planning = planning;
        this.parties = parties;
    }
}
