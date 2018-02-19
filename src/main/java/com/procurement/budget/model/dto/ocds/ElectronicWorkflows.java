package com.procurement.budget.model.dto.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "useOrdering",
        "usePayment",
        "acceptInvoicing"
})
public class ElectronicWorkflows {
    @JsonProperty("useOrdering")
    private final Boolean useOrdering;

    @JsonProperty("usePayment")
    private final Boolean usePayment;

    @JsonProperty("acceptInvoicing")
    private final Boolean acceptInvoicing;

    @JsonCreator
    public ElectronicWorkflows(@JsonProperty("useOrdering") final Boolean useOrdering,
                               @JsonProperty("usePayment") final Boolean usePayment,
                               @JsonProperty("acceptInvoicing") final Boolean acceptInvoicing) {
        this.useOrdering = useOrdering;
        this.usePayment = usePayment;
        this.acceptInvoicing = acceptInvoicing;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(useOrdering)
                .append(usePayment)
                .append(acceptInvoicing)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ElectronicWorkflows)) {
            return false;
        }
        final ElectronicWorkflows rhs = (ElectronicWorkflows) other;
        return new EqualsBuilder().append(useOrdering, rhs.useOrdering)
                .append(usePayment, rhs.usePayment)
                .append(acceptInvoicing, rhs.acceptInvoicing)
                .isEquals();
    }
}
