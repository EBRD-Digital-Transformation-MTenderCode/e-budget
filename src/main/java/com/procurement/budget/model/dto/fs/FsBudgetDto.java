package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.BudgetBreakdown;
import com.procurement.budget.model.dto.ocds.EuropeanUnionFunding;
import com.procurement.budget.model.dto.ocds.Period;
import com.procurement.budget.model.dto.ocds.Value;
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
        "project",
        "projectID",
        "uri",
        "source",
        "europeanUnionFunding",
        "isEuropeanUnionFunded",
        "verified"
})
public class FsBudgetDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("period")
    private final Period period;
    @JsonProperty("amount")
    @Valid
    @NotNull
    private final FsValue amount;
    @JsonProperty("project")
    private final String project;
    @JsonProperty("projectID")
    private final String projectID;
    @JsonProperty("uri")
    private final String uri;
    @JsonProperty("source")
    private final String source;
    @JsonProperty("europeanUnionFunding")
    @Valid
    private final EuropeanUnionFunding europeanUnionFunding;
    @JsonProperty("isEuropeanUnionFunded")
    private final Boolean isEuropeanUnionFunded;
    @JsonProperty("budgetBreakdown")
    private final List<BudgetBreakdown> budgetBreakdown;
    @JsonProperty("verified")
    private final Boolean verified;

    @JsonCreator
    public FsBudgetDto(@JsonProperty("id") final String id,
                       @JsonProperty("description") final String description,
                       @JsonProperty("period") final Period period,
                       @JsonProperty("amount") final FsValue amount,
                       @JsonProperty("project") final String project,
                       @JsonProperty("projectID") final String projectID,
                       @JsonProperty("uri") final String uri,
                       @JsonProperty("source") final String source,
                       @JsonProperty("europeanUnionFunding") final EuropeanUnionFunding europeanUnionFunding,
                       @JsonProperty("isEuropeanUnionFunded") final Boolean isEuropeanUnionFunded,
                       @JsonProperty("budgetBreakdown") final List<BudgetBreakdown> budgetBreakdown,
                       @JsonProperty("verified") final Boolean verified) {
        this.id = id;
        this.description = description;
        this.period = period;
        this.amount = amount;
        this.project = project;
        this.projectID = projectID;
        this.uri = uri;
        this.source = source;
        this.europeanUnionFunding = europeanUnionFunding;
        this.isEuropeanUnionFunded = isEuropeanUnionFunded;
        this.budgetBreakdown = budgetBreakdown;
        this.verified = verified;
    }
}
