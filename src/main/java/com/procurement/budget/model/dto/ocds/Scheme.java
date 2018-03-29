package com.procurement.budget.model.dto.ocds;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Scheme {

    CPV("CPV"),
    CPVS("CPVS"),
    GSIN("GSIN"),
    UNSPSC("UNSPSC"),
    CPC("CPC"),
    OKDP("OKDP"),
    OKPD("OKPD");

    private final static Map<String, Scheme> CONSTANTS = new HashMap<>();

    static {
        for (final Scheme c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    Scheme(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static Scheme fromValue(final String value) {
        final Scheme constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(
                    "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
        }
        return constant;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }
}
