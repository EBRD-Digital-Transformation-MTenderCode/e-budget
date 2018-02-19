package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.budget.model.dto.ocds.*;
import java.util.*;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "classification"
})
public class EiTenderDto {
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this tender process. This may be the same as the ocid, or may be " +
            "drawn from an internally held identifier for this tender.")
    private String id;

    @JsonProperty("title")
    @JsonPropertyDescription("A title for this tender. This will often be used by applications as a headline to " +
            "attract interest, and to help analysts understand the nature of this procurement.")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("A summary description of the tender. This should complement structured information " +
            "provided using the items array. Descriptions should be short and easy to read. Avoid using ALL CAPS. ")
    private final String description;

    @JsonProperty("status")
    @JsonPropertyDescription("The current status of the tender based on the [tenderStatus codelist](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#tender-status)")
    private TenderStatus status;

    @JsonProperty("classification")
    @Valid
    private final Classification classification;

    @JsonCreator
    public EiTenderDto(@JsonProperty("id") final String id,
                       @JsonProperty("title") final String title,
                       @JsonProperty("description") final String description,
                       @JsonProperty("status") final TenderStatus status,
                       @JsonProperty("classification") final Classification classification) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.classification = classification;
    }
}
