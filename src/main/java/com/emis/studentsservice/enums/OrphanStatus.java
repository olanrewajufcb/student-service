package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrphanStatus {
    NONE,
    LOST_MOTHER,
    LOST_FATHER,
    LOST_BOTH;

    @JsonCreator
    public static OrphanStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return OrphanStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for OrphanStatus: '" + value +
                            "'. Accepted values are: LOST_MOTHER, LOST_BOTH..."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
