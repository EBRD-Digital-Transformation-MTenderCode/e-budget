package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.*;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import com.procurement.budget.model.dto.ocds.TenderStatusDetails;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "status",
        "statusDetails",
        "procuringEntity"
})
public class FsTenderDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private TenderStatus status;

    @JsonProperty("statusDetails")
    private TenderStatusDetails statusDetails;

    @JsonProperty("procuringEntity")
    @NotNull
    private FsOrganizationReferenceDto procuringEntity;

    @JsonCreator
    public FsTenderDto(@JsonProperty("id") final String id,
                       @JsonProperty("status") final TenderStatus status,
                       @JsonProperty("statusDetails") final TenderStatusDetails statusDetails,
                       @JsonProperty("procuringEntity") final FsOrganizationReferenceDto procuringEntity) {
        this.id = id;
        this.status = status;
        this.statusDetails = statusDetails;
        this.procuringEntity = procuringEntity;
    }
}
