package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ocds.Classification;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "budgetBreakdown",
        "tenderPeriod",
        "classification"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckRequestDto {

    @NotEmpty
    @JsonProperty("budgetBreakdown")
    private final List<CheckBudgetBreakdownDto> budgetBreakdown;

    @NotNull
    @JsonProperty("tenderPeriod")
    private final CheckPeriodDto tenderPeriod;

    @NotNull
    @JsonProperty("classification")
    private final Classification classification;

    @JsonCreator
    public CheckRequestDto(@JsonProperty("budgetBreakdown") final List<CheckBudgetBreakdownDto> budgetBreakdown,
                           @JsonProperty("tenderPeriod") final CheckPeriodDto tenderPeriod,
                           @JsonProperty("classification") final Classification classification) {
        this.budgetBreakdown = budgetBreakdown;
        this.tenderPeriod = tenderPeriod;
        this.classification = classification;
    }
}
