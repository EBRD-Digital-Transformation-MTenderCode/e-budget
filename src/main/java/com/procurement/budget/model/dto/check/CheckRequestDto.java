package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "budgetBreakdown",
        "tenderPeriod"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckRequestDto {

    @JsonProperty("budgetBreakdown")
    @NotEmpty
    private final List<CheckBudgetBreakdownDto> budgetBreakdown;

    @JsonProperty("tenderPeriod")
    @NotNull
    private final CheckPeriodDto tenderPeriod;

    @JsonCreator
    public CheckRequestDto(@JsonProperty("budgetBreakdown") final List<CheckBudgetBreakdownDto> budgetBreakdown,
                           @JsonProperty("tenderPeriod") final CheckPeriodDto tenderPeriod) {
        this.budgetBreakdown = budgetBreakdown;
        this.tenderPeriod = tenderPeriod;
    }
}
