package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisabilityLevel {

    NONE,
    MINOR,
    MODERATE,
    SEVERE;

    @JsonCreator
    public static DisabilityLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return DisabilityLevel.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for DisabilityLevel: '" + value +
                            "'. Accepted values are: MINOR, MODERATE, SEVERE"
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
