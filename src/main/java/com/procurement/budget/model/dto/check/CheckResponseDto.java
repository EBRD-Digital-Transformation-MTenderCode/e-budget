package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.budget.model.dto.ei.EiOrganizationReferenceDto;
import com.procurement.budget.model.dto.fs.FsOrganizationReferenceDto;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
@JsonPropertyOrder({
        "budgetBreakdown",
        "ei",
        "funder",
        "payer",
        "buyer"
})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CheckResponseDto {
    @JsonProperty("budgetBreakdown")
    private final List<CheckBudgetBreakdownDto> budgetBreakdown;
    @JsonProperty("ei")
    private final Set<String> ei;
    @JsonProperty("funder")
    private Set<FsOrganizationReferenceDto> funder;
    @JsonProperty("payer")
    private Set<FsOrganizationReferenceDto> payer;
    @JsonProperty("buyer")
    private Set<EiOrganizationReferenceDto> buyer;

    @JsonCreator
    public CheckResponseDto(@JsonProperty("budgetBreakdown") final List<CheckBudgetBreakdownDto> budgetBreakdown,
                            @JsonProperty("ei") final Set<String> ei,
                            @JsonProperty("funder") final HashSet<FsOrganizationReferenceDto> funder,
                            @JsonProperty("payer") final HashSet<FsOrganizationReferenceDto> payer,
                            @JsonProperty("buyer") final HashSet<EiOrganizationReferenceDto> buyer) {
        this.budgetBreakdown = budgetBreakdown;
        this.ei = ei;
        this.funder = funder;
        this.payer = payer;
        this.buyer = buyer;
    }
}
