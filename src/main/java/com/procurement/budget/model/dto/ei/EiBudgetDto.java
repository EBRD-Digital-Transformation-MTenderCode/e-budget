package com.procurement.budget.model.dto.ei;

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
        "period",
        "amount"
})
public class EiBudgetDto {

    @JsonProperty("id")
    private String id;

    @Valid
    @NotNull
    @JsonProperty("period")
    private final Period period;

    @Valid
    @NotNull
    @JsonProperty("amount")
    private final EiValue amount;

    @JsonCreator
    public EiBudgetDto(@JsonProperty("id") final String id,
                       @JsonProperty("period") final Period period,
                       @JsonProperty("amount") final EiValue amount
    ) {
        this.id = id;
        this.period = period;
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(period)
                .append(amount)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EiBudgetDto)) {
            return false;
        }
        final EiBudgetDto rhs = (EiBudgetDto) other;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(period, rhs.period)
                .append(amount, rhs.amount)
                .isEquals();
    }
}
