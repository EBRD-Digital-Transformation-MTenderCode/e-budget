package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.databinding.LocalDateTimeDeserializer;
import com.procurement.budget.model.dto.databinding.LocalDateTimeSerializer;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import java.time.LocalDateTime;
import javax.validation.Valid;
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
    @JsonProperty("tender")
    @NotNull
    private FsTenderDto tender;
    @JsonProperty("planning")
    @NotNull
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
