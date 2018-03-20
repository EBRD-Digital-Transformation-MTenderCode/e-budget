package com.procurement.budget.model.dto.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "identifier",
        "address",
        "additionalIdentifiers",
        "contactPoint",
        "details"
})
public class OrganizationReference {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @Size(min = 1)
    @NotNull
    private final String name;

    @JsonProperty("identifier")
    @Valid
    private Identifier identifier;

    @JsonProperty("address")
    @Valid
    private Address address;

    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @Valid
    private Set<Identifier> additionalIdentifiers;

    @JsonProperty("contactPoint")
    @Valid
    private ContactPoint contactPoint;

    @JsonProperty("details")
    private Details details;

    @JsonCreator
    public OrganizationReference(@JsonProperty("id") final String id,
                                 @JsonProperty("name") final String name,
                                 @JsonProperty("identifier") final Identifier identifier,
                                 @JsonProperty("address") final Address address,
                                 @JsonProperty("additionalIdentifiers") final LinkedHashSet<Identifier>
                                         additionalIdentifiers,
                                 @JsonProperty("contactPoint") final ContactPoint contactPoint,
                                 @JsonProperty("details") final Details details) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.address = address;
        this.additionalIdentifiers = additionalIdentifiers;
        this.contactPoint = contactPoint;
        this.details = details;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(id)
                .append(identifier)
                .append(address)
                .append(additionalIdentifiers)
                .append(contactPoint)
                .append(details)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof OrganizationReference)) {
            return false;
        }
        final OrganizationReference rhs = (OrganizationReference) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .append(identifier, rhs.identifier)
                .append(address, rhs.address)
                .append(additionalIdentifiers, rhs.additionalIdentifiers)
                .append(contactPoint, rhs.contactPoint)
                .append(details, rhs.details)
                .isEquals();
    }
}
