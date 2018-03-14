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
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "tender",
        "planning",
        "buyer",
})

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FsRequestDto {
    @JsonProperty("tender")
    @NotNull
    private FsTenderDto tender;
    @JsonProperty("planning")
    @NotNull
    private FsPlanningDto planning;
    @JsonProperty("buyer")
    @Valid
    private final OrganizationReference buyer;

    @JsonCreator
    public FsRequestDto(@JsonProperty("tender") final FsTenderDto tender,
                        @JsonProperty("planning") final FsPlanningDto planning,
                        @JsonProperty("buyer") final OrganizationReference buyer) {
        this.tender = tender;
        this.planning = planning;
        this.buyer = buyer;
    }
}
