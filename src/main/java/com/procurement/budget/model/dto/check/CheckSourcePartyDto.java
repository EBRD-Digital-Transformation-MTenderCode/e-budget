package com.procurement.budget.model.dto.check;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name"

})
public class CheckSourcePartyDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private final String name;

    @JsonCreator
    public CheckSourcePartyDto(@JsonProperty("id") final String id,
                               @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CheckSourcePartyDto)) {
            return false;
        }
        final CheckSourcePartyDto rhs = (CheckSourcePartyDto) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .isEquals();
    }
}
