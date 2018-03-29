
package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.budget.model.dto.ocds.Currency;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "currency"
})
public class EiValue {

    @JsonProperty("amount")
    private final Double amount;

    @NotNull
    @JsonProperty("currency")
    private final Currency currency;

    @JsonCreator
    public EiValue(@JsonProperty("amount") final Double amount,
                   @JsonProperty("currency") final Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amount)
                                    .append(currency)
                                    .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EiValue)) {
            return false;
        }
        final EiValue rhs = (EiValue) other;
        return new EqualsBuilder().append(amount, rhs.amount)
                                  .append(currency, rhs.currency)
                                  .isEquals();
    }
}
