package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
        "amount",
        "period",
        "sourceParty"
})
public class CheckBudgetBreakdownDto {

    @NotNull
    @JsonProperty("id")
    private final String id;

    @JsonProperty("description")
    private String description;

    @Valid
    @NotNull
    @JsonProperty("amount")
    private final CheckValueDto amount;

    @Valid
    @JsonProperty("period")
    private Period period;

    @Valid
    @JsonProperty("sourceParty")
    private CheckSourcePartyDto sourceParty;

    @JsonCreator
    public CheckBudgetBreakdownDto(@JsonProperty("id") final String id,
                                   @JsonProperty("description") final String description,
                                   @JsonProperty("amount") final CheckValueDto amount,
                                   @JsonProperty("period") final Period period,
                                   @JsonProperty("sourceParty") final CheckSourcePartyDto sourceParty) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.period = period;
        this.sourceParty = sourceParty;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(description)
                .append(amount)
                .append(period)
                .append(sourceParty)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CheckBudgetBreakdownDto)) {
            return false;
        }
        final CheckBudgetBreakdownDto rhs = (CheckBudgetBreakdownDto) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(description, rhs.description)
                .append(amount, rhs.amount)
                .append(period, rhs.period)
                .append(sourceParty, rhs.sourceParty)
                .isEquals();
    }
}
