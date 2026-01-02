package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StudentStatus {
    ACTIVE,
    INACTIVE,
    GRADUATED,
    TRANSFERRED,
    WITHDRAWN,
    SUSPENDED,
    ENROLLED,
    DROPPED_OUT,
    ADMITTED,
    TRANSFERRED_IN,
    TRANSFERRED_OUT,
    PROMOTED;

    @JsonCreator
    public static StudentStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return StudentStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for StudentStatus: '" + value +
                            "'. Accepted values are: ENROLLED, PROMOTED..."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
