package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.budget.model.dto.ocds.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    private String id;

    @JsonProperty("title")
    @NotNull
    private final String title;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("status")
    private TenderStatus status;

    @JsonProperty("statusDetails")
    private TenderStatusDetails statusDetails;

    @JsonProperty("classification")
    @Valid
    @NotNull
    private final Classification classification;

    @JsonCreator
    public EiTenderDto(@JsonProperty("id") final String id,
                       @JsonProperty("title") final String title,
                       @JsonProperty("description") final String description,
                       @JsonProperty("status") final TenderStatus status,
                       @JsonProperty("statusDetails") final TenderStatusDetails statusDetails,
                       @JsonProperty("classification") final Classification classification) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.statusDetails = statusDetails;
        this.classification = classification;
    }
}
