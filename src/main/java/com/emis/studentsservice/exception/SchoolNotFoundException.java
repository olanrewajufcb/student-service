package com.emis.studentsservice.exception;

import lombok.Getter;

@Getter
public class SchoolNotFoundException extends RuntimeException {
    public SchoolNotFoundException(String schoolId) {
        super("School not found with id: " + schoolId);
    }
}