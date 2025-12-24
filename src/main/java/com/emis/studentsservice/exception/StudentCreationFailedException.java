package com.emis.studentsservice.exception;

public class StudentCreationFailedException extends  RuntimeException {
    public StudentCreationFailedException(String reason) {
        super("Student creation failed: " + reason);
    }
}
