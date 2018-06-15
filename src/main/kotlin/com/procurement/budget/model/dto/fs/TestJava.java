package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestJava {

    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("isEuropeanUnionFunded")
    private Boolean isEuropeanUnionFunded;

    @JsonCreator
    public TestJava(@JsonProperty("id") final String id,
                    @JsonProperty("isEuropeanUnionFunded") final Boolean isEuropeanUnionFunded) {
        this.id = id;
        this.isEuropeanUnionFunded = isEuropeanUnionFunded;
    }
}
