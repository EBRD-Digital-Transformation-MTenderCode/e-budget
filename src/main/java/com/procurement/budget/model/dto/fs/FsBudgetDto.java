package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.*;
import com.procurement.budget.model.dto.ocds.BudgetBreakdown;
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
import com.procurement.budget.model.dto.ocds.Period;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("period")
    @NotNull
    private final Period period;
    @JsonProperty("amount")
    @Valid
    @NotNull
    private final FsValue amount;
    @JsonProperty("europeanUnionFunding")
    @Valid
    private final EuropeanUnionFunding europeanUnionFunding;
    @JsonProperty("isEuropeanUnionFunded")
    @NotNull
    private final Boolean isEuropeanUnionFunded;
    @JsonProperty("verified")
    private Boolean verified;
    @JsonProperty("sourceEntity")
    @Valid
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
}
