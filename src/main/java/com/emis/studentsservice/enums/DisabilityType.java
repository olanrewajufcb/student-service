package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisabilityType {

    NONE,
    BLIND,
    VISUALLY_IMPAIRED,
    HEARING_IMPAIRED,
    SPEECH_IMPAIRED,
    MENTALLY_CHALLENGED,
    PHYSICAL_CHALLENGED,
    AUTISM,
    OTHER;

    @JsonCreator
    public static DisabilityType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return DisabilityType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for DisabilityType: '" + value +
                            "'. Accepted values are: BLIND, AUTISM, HEARING_IMPAIRED..."
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
