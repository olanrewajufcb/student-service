package com.emis.studentsservice.exception;

import lombok.Getter;

@Getter
public class StudentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String fieldName;
    private final Object searchValue;

    public StudentNotFoundException(String message) {
        super(message);
        this.fieldName = null;
        this.searchValue = null;
    }

    public StudentNotFoundException(String message, String fieldName, Object searchValue) {
        super(message);
        this.fieldName = fieldName;
        this.searchValue = searchValue;
    }
}