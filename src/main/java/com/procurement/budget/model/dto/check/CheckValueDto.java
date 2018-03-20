package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
public class CheckValueDto {
    @JsonProperty("amount")
    @NotNull
    private final Double amount;
    @JsonProperty("currency")
    @NotNull
    private final Currency currency;

    @JsonCreator
    public CheckValueDto(@JsonProperty("amount") final Double amount,
                         @JsonProperty("currency") final Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
