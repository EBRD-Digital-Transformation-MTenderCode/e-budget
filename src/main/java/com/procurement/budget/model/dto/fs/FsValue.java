
package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.*;
import com.procurement.budget.model.dto.ocds.Currency;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
public class FsValue {
    @JsonProperty("amount")
    @NotNull
    private final Double amount;
    @JsonProperty("currency")
    @NotNull
    private final Currency currency;

    @JsonCreator
    public FsValue(@JsonProperty("amount") final Double amount,
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
        if (!(other instanceof FsValue)) {
            return false;
        }
        final FsValue rhs = (FsValue) other;
        return new EqualsBuilder().append(amount, rhs.amount)
                                  .append(currency, rhs.currency)
                                  .isEquals();
    }
}
