package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckPlanningDto {

    @JsonProperty("budget")
    private CheckBudgetDto budget;

    @JsonCreator
    public CheckPlanningDto(@JsonProperty("budget") final CheckBudgetDto budget) {
        this.budget = budget;
    }
}
