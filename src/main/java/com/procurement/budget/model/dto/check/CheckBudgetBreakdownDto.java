package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.BudgetBreakdown;
import com.procurement.budget.model.dto.ocds.OrganizationReference;
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
        "amount",
        "period",
        "sourceParty"
})
public class CheckBudgetBreakdownDto {
    @JsonProperty("id")
    @NotNull
    private final String id;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("amount")
    @Valid
    @NotNull
    private final Value amount;

    @JsonProperty("period")
    private final Period period;

    @JsonProperty("sourceParty")
    private OrganizationReference sourceParty;

    @JsonCreator
    public CheckBudgetBreakdownDto(@JsonProperty("id") final String id,
                           @JsonProperty("description") final String description,
                           @JsonProperty("amount") final Value amount,
                           @JsonProperty("period") final Period period,
                           @JsonProperty("sourceParty") final OrganizationReference sourceParty) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.period = period;
        this.sourceParty = sourceParty;
    }
}
