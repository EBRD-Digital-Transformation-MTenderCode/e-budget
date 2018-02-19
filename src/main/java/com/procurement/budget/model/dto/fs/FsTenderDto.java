package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.*;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "status"
})
public class FsTenderDto {
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this tender process. This may be the same as the ocid, or may be " +
            "drawn from an internally held identifier for this tender.")
    private String id;

    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the tender based on the [tenderStatus codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#tender-status)")
    private TenderStatus status;

    @JsonCreator
    public FsTenderDto(@JsonProperty("id") final String id,
                       @JsonProperty("status") final TenderStatus status) {
        this.id = id;
        this.status = status;
    }
}
