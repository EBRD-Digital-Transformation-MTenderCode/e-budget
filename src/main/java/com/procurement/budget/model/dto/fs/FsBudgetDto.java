package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding;
import com.procurement.budget.model.dto.ocds.Period;
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
        "description",
        "period",
        "amount",
        "europeanUnionFunding",
        "isEuropeanUnionFunded",
        "verified",
        "sourceEntity",
        "verificationDetails"
})
public class FsBudgetDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("description")
    private final String description;

    @NotNull
    @JsonProperty("period")
    private final Period period;

    @Valid
    @NotNull
    @JsonProperty("amount")
    private final FsValue amount;

    @Valid
    @JsonProperty("europeanUnionFunding")
    private final EuropeanUnionFunding europeanUnionFunding;

    @NotNull
    @JsonProperty("isEuropeanUnionFunded")
    private final Boolean isEuropeanUnionFunded;

    @JsonProperty("verified")
    private Boolean verified;

    @Valid
    @JsonProperty("sourceEntity")
    private FsOrganizationReferenceDto sourceEntity;

    @JsonProperty("verificationDetails")
    private final String verificationDetails;

    @JsonCreator
    public FsBudgetDto(@JsonProperty("id") final String id,
                       @JsonProperty("description") final String description,
                       @JsonProperty("period") final Period period,
                       @JsonProperty("amount") final FsValue amount,
                       @JsonProperty("europeanUnionFunding") final EuropeanUnionFunding europeanUnionFunding,
                       @JsonProperty("isEuropeanUnionFunded") final Boolean isEuropeanUnionFunded,
                       @JsonProperty("verified") final Boolean verified,
                       @JsonProperty("sourceEntity") final FsOrganizationReferenceDto sourceEntity,
                       @JsonProperty("verificationDetails") final String verificationDetails) {
        this.id = id;
        this.description = description;
        this.period = period;
        this.amount = amount;
        this.europeanUnionFunding = europeanUnionFunding;
        this.isEuropeanUnionFunded = isEuropeanUnionFunded;
        this.verified = verified == null ? false : verified;
        this.sourceEntity = sourceEntity;
        this.verificationDetails = verificationDetails;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(description)
                .append(period)
                .append(amount)
                .append(europeanUnionFunding)
                .append(isEuropeanUnionFunded)
                .append(verified)
                .append(sourceEntity)
                .append(verificationDetails)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FsBudgetDto)) {
            return false;
        }
        final FsBudgetDto rhs = (FsBudgetDto) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(description, rhs.description)
                .append(period, rhs.period)
                .append(amount, rhs.amount)
                .append(europeanUnionFunding, rhs.europeanUnionFunding)
                .append(isEuropeanUnionFunded, rhs.isEuropeanUnionFunded)
                .append(verified, rhs.verified)
                .append(sourceEntity, rhs.sourceEntity)
                .append(verificationDetails, rhs.verificationDetails)
                .isEquals();
    }
}
