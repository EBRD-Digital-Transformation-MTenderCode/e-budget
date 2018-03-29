package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.TenderStatus;
import com.procurement.budget.model.dto.ocds.TenderStatusDetails;
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
        "status",
        "statusDetails",
        "procuringEntity"
})
public class FsTenderDto {

    @NotNull
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private TenderStatus status;

    @JsonProperty("statusDetails")
    private TenderStatusDetails statusDetails;

    @NotNull
    @JsonProperty("procuringEntity")
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(status)
                .append(statusDetails)
                .append(procuringEntity)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FsTenderDto)) {
            return false;
        }
        final FsTenderDto rhs = (FsTenderDto) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(status, rhs.status)
                .append(statusDetails, rhs.statusDetails)
                .append(procuringEntity, rhs.procuringEntity)
                .isEquals();
    }
}
