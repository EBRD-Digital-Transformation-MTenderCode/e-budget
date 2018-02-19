package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.Period;
import com.procurement.budget.model.dto.ocds.Value;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("amount")
    @Valid
    @NotNull
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
}
