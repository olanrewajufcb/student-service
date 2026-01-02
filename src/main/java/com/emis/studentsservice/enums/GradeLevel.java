package com.emis.studentsservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GradeLevel {
    PRE_NURSERY("PRE NURSERY"),
    NURSERY_1 ("NURSERY 1"),
    NURSERY_2("NURSERY 2"),
    PRIMARY_1("PRIMARY 1"),
    PRIMARY_2("PRIMARY 2"),
    PRIMARY_3("PRIMARY 3"),
    PRIMARY_4("PRIMARY 4"),
    PRIMARY_5("PRIMARY 5"),
    PRIMARY_6("PRIMARY 6"),
    JSS_1("JSS 1"),
    JSS_2("JSS 2"),
    JSS_3("JSS 3"),
    SS_1("SS 1"),
    SS_2("SS 2"),
    SS_3("SS 3");

    GradeLevel(String value) {
        this.value = value;
    }

    private final String value;
    @JsonCreator
    public static GradeLevel fromString(String value) {
       for (GradeLevel gradeLevel : GradeLevel.values()) {
           if (gradeLevel.name().equalsIgnoreCase(value)) {
               return gradeLevel;
           }
       }
       return null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
