package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.*;
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
        "token",
        "ocid",
        "tender",
        "planning",
        "buyer"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class EiDto {

    @JsonProperty("token")
    private String token;

    @JsonProperty("ocid")
    private String ocId;

    @Valid
    @NotNull
    @JsonProperty("tender")
    private EiTenderDto tender;

    @Valid
    @NotNull
    @JsonProperty("planning")
    private EiPlanningDto planning;

    @Valid
    @NotNull
    @JsonProperty("buyer")
    private final EiOrganizationReferenceDto buyer;

    @JsonCreator
    public EiDto(@JsonProperty("token") final String token,
                 @JsonProperty("ocid") final String ocId,
                 @JsonProperty("tender") final EiTenderDto tender,
                 @JsonProperty("planning") final EiPlanningDto planning,
                 @JsonProperty("buyer") final EiOrganizationReferenceDto buyer) {
        this.token = token;
        this.ocId = ocId;
        this.tender = tender;
        this.planning = planning;
        this.buyer = buyer;
    }
}
