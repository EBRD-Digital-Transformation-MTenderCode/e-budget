package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.fs.FsPlanningDto;
import com.procurement.budget.model.dto.fs.FsTenderDto;
import com.procurement.budget.model.dto.ocds.BudgetBreakdown;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CheckBudgetDto {

    @JsonProperty("budgetBreakdown")
    @NotEmpty
    private final List<CheckBudgetBreakdownDto> budgetBreakdown;

    @JsonCreator
    public CheckBudgetDto(@JsonProperty("budgetBreakdown") final List<CheckBudgetBreakdownDto> budgetBreakdown) {
        this.budgetBreakdown = budgetBreakdown;
    }
}
