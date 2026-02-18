package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnrollmentType {
    NEW,
    INITIAL_ENROLLMENT,
    GRADE_PROMOTION,
    TRANSFERRED_IN,
    TRANSFERRED_OUT,
    RETENTION;

    @JsonCreator
    public static EnrollmentType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return EnrollmentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for EnrollmentType: '" + value +
                            "'. Accepted values are: TRANSFER_IN, INITIAL_ENROLLMENT..."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
