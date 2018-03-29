package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({
        "token",
        "ocid",
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

    @NotNull
    @JsonProperty("tender")
    private FsTenderDto tender;

    @NotNull
    @JsonProperty("planning")
    private FsPlanningDto planning;

    @JsonProperty("funder")
    private FsOrganizationReferenceDto funder;

    @JsonProperty("payer")
    private FsOrganizationReferenceDto payer;

    @JsonCreator
    public FsDto(@JsonProperty("token") final String token,
                 @JsonProperty("ocid") final String ocId,
                 @JsonProperty("tender") final FsTenderDto tender,
                 @JsonProperty("planning") final FsPlanningDto planning,
                 @JsonProperty("funder") final FsOrganizationReferenceDto funder,
                 @JsonProperty("payer") final FsOrganizationReferenceDto payer) {
        this.token = token;
        this.ocId = ocId;
        this.tender = tender;
        this.planning = planning;
        this.funder = funder;
        this.payer = payer;
    }
}
