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
        "date",
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
    @JsonProperty("date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @JsonProperty("tender")
    @NotNull
    @Valid
    private EiTenderDto tender;
    @JsonProperty("planning")
    @NotNull
    @Valid
    private EiPlanningDto planning;
    @JsonProperty("buyer")
    @NotNull
    @Valid
    private final EiOrganizationReferenceDto buyer;

    @JsonCreator
    public EiDto(@JsonProperty("token") final String token,
                 @JsonProperty("ocid") final String ocId,
                 @JsonProperty("date") final LocalDateTime date,
                 @JsonProperty("tender") final EiTenderDto tender,
                 @JsonProperty("planning") final EiPlanningDto planning,
                 @JsonProperty("buyer") final EiOrganizationReferenceDto buyer) {
        this.token = token;
        this.ocId = ocId;
        this.date = date;
        this.tender = tender;
        this.planning = planning;
        this.buyer = buyer;
    }
}
