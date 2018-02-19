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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "ocid",
        "id",
        "date",
        "planning",
        "tender",
        "parties"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EiDto {
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @JsonProperty("planning")
    @NotNull
    @Valid
    private EiPlanningDto planning;
    @JsonProperty("tender")
    @NotNull
    @Valid
    private EiTenderDto tender;
    @JsonProperty("parties")
    @NotNull
    @Valid
    private List<Organization> parties;

    @JsonCreator
    public EiDto(@JsonProperty("ocid") final String ocId,
                 @JsonProperty("id") final String id,
                 @JsonProperty("date") final LocalDateTime date,
                 @JsonProperty("planning") final EiPlanningDto planning,
                 @JsonProperty("tender") final EiTenderDto tender,
                 @JsonProperty("parties") final List<Organization> parties) {
        this.ocId = ocId;
        this.id = id;
        this.date = date;
        this.planning = planning;
        this.tender = tender;
        this.parties = parties;
    }
}
