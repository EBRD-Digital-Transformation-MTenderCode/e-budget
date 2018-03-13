package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.*;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "status",
        "procuringEntity"
})
public class FsTenderDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private TenderStatus status;

    @JsonProperty("procuringEntity")
    private OrganizationReference procuringEntity;

    @JsonCreator
    public FsTenderDto(@JsonProperty("id") final String id,
                       @JsonProperty("status") final TenderStatus status,
                       @JsonProperty("procuringEntity") final OrganizationReference procuringEntity) {
        this.id = id;
        this.status = status;
        this.procuringEntity = procuringEntity;
    }
}
