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
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        "funder",
        "payer"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FsDto {
    @JsonProperty("token")
    private String token;
    @JsonProperty("ocid")
    private String ocId;
    @JsonProperty("date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @JsonProperty("tender")
    @NotNull
    private FsTenderDto tender;
    @JsonProperty("planning")
    @NotNull
    private FsPlanningDto planning;
    @JsonProperty("funder")
    @NotNull
    @Valid
    private final OrganizationReference funder;
    @JsonProperty("payer")
    @NotNull
    @Valid
    private final OrganizationReference payer;

    @JsonCreator
    public FsDto(@JsonProperty("token") final String token,
                         @JsonProperty("ocid") final String ocId,
                         @JsonProperty("date") final LocalDateTime date,
                         @JsonProperty("tender") final FsTenderDto tender,
                         @JsonProperty("planning") final FsPlanningDto planning,
                         @JsonProperty("funder") final OrganizationReference funder,
                         @JsonProperty("payer") final OrganizationReference payer) {
        this.token = token;
        this.ocId = ocId;
        this.date = date;
        this.tender = tender;
        this.planning = planning;
        this.funder = funder;
        this.payer = payer;
    }
}
