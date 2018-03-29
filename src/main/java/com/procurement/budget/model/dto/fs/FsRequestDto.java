package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "tender",
        "planning",
        "buyer"
})

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FsRequestDto {

    @NotNull
    @JsonProperty("tender")
    private FsTenderDto tender;

    @NotNull
    @JsonProperty("planning")
    private FsPlanningDto planning;

    @Valid
    @JsonProperty("buyer")
    private final FsOrganizationReferenceDto buyer;

    @JsonCreator
    public FsRequestDto(@JsonProperty("tender") final FsTenderDto tender,
                        @JsonProperty("planning") final FsPlanningDto planning,
                        @JsonProperty("buyer") final FsOrganizationReferenceDto buyer) {
        this.tender = tender;
        this.planning = planning;
        this.buyer = buyer;
    }
}
