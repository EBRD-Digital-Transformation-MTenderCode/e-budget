package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "budget",
        "rationale"
})
public class EiPlanningDto {

    @Valid
    @NotNull
    @JsonProperty("budget")
    private final EiBudgetDto budget;

    @JsonProperty("rationale")
    private final String rationale;

    @JsonCreator
    public EiPlanningDto(@JsonProperty("budget") final EiBudgetDto budget,
                         @JsonProperty("rationale") final String rationale) {
        this.budget = budget;
        this.rationale = rationale;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(budget)
                .append(rationale)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EiPlanningDto)) {
            return false;
        }
        final EiPlanningDto rhs = (EiPlanningDto) other;
        return new EqualsBuilder()
                .append(budget, rhs.budget)
                .append(rationale, rhs.rationale)
                .isEquals();
    }
}
