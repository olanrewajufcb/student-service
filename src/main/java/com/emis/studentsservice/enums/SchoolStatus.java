package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SchoolStatus {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static SchoolStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return SchoolStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for SchoolStatus: '" + value +
                            "'. Accepted values are: ACTIVE, INACTIVE, OTHERS"
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
