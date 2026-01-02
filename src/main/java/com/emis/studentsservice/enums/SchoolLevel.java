package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchoolLevel {
    PRIMARY,
    JUNIOR_SECONDARY,
    SENIOR_SECONDARY;

    @JsonCreator
    public static SchoolLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return SchoolLevel.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for SchoolLevel: '" + value +
                            "'. Accepted values are: PRIMARY, SECONDARY..."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
