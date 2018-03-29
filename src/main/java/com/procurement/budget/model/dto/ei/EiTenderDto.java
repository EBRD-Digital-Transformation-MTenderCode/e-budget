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
        "statusDetails",
        "classification"
})
public class EiTenderDto {

    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("title")
    private final String title;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("status")
    private TenderStatus status;

    @JsonProperty("statusDetails")
    private TenderStatusDetails statusDetails;

    @Valid
    @NotNull
    @JsonProperty("classification")
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(title)
                .append(description)
                .append(status)
                .append(statusDetails)
                .append(classification)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EiTenderDto)) {
            return false;
        }
        final EiTenderDto rhs = (EiTenderDto) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(classification, rhs.classification)
                .isEquals();
    }

}
