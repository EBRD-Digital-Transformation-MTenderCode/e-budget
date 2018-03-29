package com.procurement.budget.model.dto.fs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.budget.model.dto.ocds.Address;
import com.procurement.budget.model.dto.ocds.ContactPoint;
import com.procurement.budget.model.dto.ocds.Identifier;
import java.util.HashSet;
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
        "id",
        "name",
        "identifier",
        "address",
        "additionalIdentifiers",
        "contactPoint"
})
public class FsOrganizationReferenceDto {

    @JsonProperty("id")
    private String id;

    @Size(min = 1)
    @NotNull
    @JsonProperty("name")
    private final String name;

    @Valid
    @NotNull
    @JsonProperty("identifier")
    private Identifier identifier;

    @Valid
    @NotNull
    @JsonProperty("address")
    private Address address;

    @Valid
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonProperty("additionalIdentifiers")
    private Set<Identifier> additionalIdentifiers;

    @Valid
    @NotNull
    @JsonProperty("contactPoint")
    private ContactPoint contactPoint;

    @JsonCreator
    public FsOrganizationReferenceDto(@JsonProperty("id") final String id,
                                      @JsonProperty("name") final String name,
                                      @JsonProperty("identifier") final Identifier identifier,
                                      @JsonProperty("address") final Address address,
                                      @JsonProperty("additionalIdentifiers") final HashSet<Identifier> additionalIdentifiers,
                                      @JsonProperty("contactPoint") final ContactPoint contactPoint) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.address = address;
        this.additionalIdentifiers = additionalIdentifiers;
        this.contactPoint = contactPoint;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(id)
                .append(identifier)
                .append(address)
                .append(additionalIdentifiers)
                .append(contactPoint)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FsOrganizationReferenceDto)) {
            return false;
        }
        final FsOrganizationReferenceDto rhs = (FsOrganizationReferenceDto) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .append(identifier, rhs.identifier)
                .append(address, rhs.address)
                .append(additionalIdentifiers, rhs.additionalIdentifiers)
                .append(contactPoint, rhs.contactPoint)
                .isEquals();
    }
}
