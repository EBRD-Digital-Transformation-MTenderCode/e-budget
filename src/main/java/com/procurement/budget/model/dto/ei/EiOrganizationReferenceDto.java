package com.procurement.budget.model.dto.ei;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.procurement.budget.model.dto.ocds.Address;
import com.procurement.budget.model.dto.ocds.ContactPoint;
import com.procurement.budget.model.dto.ocds.Details;
import com.procurement.budget.model.dto.ocds.Identifier;
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
        "details",
        "buyerProfile"
})
public class EiOrganizationReferenceDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @Size(min = 1)
    @NotNull
    private final String name;

    @JsonProperty("identifier")
    @Valid
    @NotNull
    private Identifier identifier;

    @JsonProperty("address")
    @Valid
    @NotNull
    private Address address;

    @JsonProperty("additionalIdentifiers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @Valid
    private Set<Identifier> additionalIdentifiers;

    @JsonProperty("contactPoint")
    @Valid
    @NotNull
    private ContactPoint contactPoint;

    @JsonProperty("details")
    @NotNull
    private Details details;

    @JsonProperty("buyerProfile")
    private final String buyerProfile;

    @JsonCreator
    public EiOrganizationReferenceDto(@JsonProperty("id") final String id,
                                      @JsonProperty("name") final String name,
                                      @JsonProperty("identifier") final Identifier identifier,
                                      @JsonProperty("address") final Address address,
                                      @JsonProperty("additionalIdentifiers") final LinkedHashSet<Identifier>
                                         additionalIdentifiers,
                                      @JsonProperty("contactPoint") final ContactPoint contactPoint,
                                      @JsonProperty("details") final Details details,
                                      @JsonProperty("buyerProfile") final String buyerProfile) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.address = address;
        this.additionalIdentifiers = additionalIdentifiers;
        this.contactPoint = contactPoint;
        this.details = details;
        this.buyerProfile = buyerProfile;
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
                .append(buyerProfile)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EiOrganizationReferenceDto)) {
            return false;
        }
        final EiOrganizationReferenceDto rhs = (EiOrganizationReferenceDto) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(id, rhs.id)
                .append(identifier, rhs.identifier)
                .append(address, rhs.address)
                .append(additionalIdentifiers, rhs.additionalIdentifiers)
                .append(contactPoint, rhs.contactPoint)
                .append(details, rhs.details)
                .append(buyerProfile, rhs.buyerProfile)
                .isEquals();
    }
}
