package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
        "amount"
})
public class CheckBudgetBreakdownDto {
    @JsonProperty("id")
    @NotNull
    private final String id;
    @JsonProperty("amount")
    @Valid
    @NotNull
    private final CheckValueDto amount;

    @JsonCreator
    public CheckBudgetBreakdownDto(@JsonProperty("id") final String id,
                                   @JsonProperty("amount") final CheckValueDto amount) {
        this.id = id;
        this.amount = amount;
    }
}
