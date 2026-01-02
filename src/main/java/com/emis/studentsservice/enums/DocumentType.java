package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    BIRTH_CERTIFICATE,
    IMMUNIZATION_RECORD,
    TRANSCRIPT,
    REPORT_CARD,
    PHYSICAL_EXAM,
    PHOTO_ID,
    PROOF_OF_RESIDENCE,
    PARENT_GUARDIAN_ID,
    CUSTODY_AGREEMENT,
    OTHER;

    @JsonCreator
    public static DocumentType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return DocumentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for DocumentType: '" + value +
                            "'. Accepted values are: PHOTO_ID, CUSTODY_AGREEMENT, BIRTH_CERTIFICATE"
            );
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
