package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.budget.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.budget.model.dto.ocds.Organization;
import com.procurement.budget.model.dto.ocds.Tender;
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
        "tender",
        "parties",
        "planning"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FsDto {
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @JsonProperty("tender")
    private FsTenderDto tender;
    @JsonProperty("planning")
    @NotNull
    @Valid
    private FsPlanningDto planning;
    @JsonProperty("parties")
    @NotNull
    @Valid
    private List<Organization> parties;

    @JsonCreator
    public FsDto(@JsonProperty("ocid") final String ocId,
                 @JsonProperty("id") final String id,
                 @JsonProperty("date") final LocalDateTime date,
                 @JsonProperty("tender") final FsTenderDto tender,
                 @JsonProperty("planning") final FsPlanningDto planning,
                 @JsonProperty("parties") final List<Organization> parties) {
        this.ocId = ocId;
        this.id = id;
        this.date = date;
        this.tender = tender;
        this.planning = planning;
        this.parties = parties;
    }
}
