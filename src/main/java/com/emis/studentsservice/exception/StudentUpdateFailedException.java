package com.emis.studentsservice.exception;

public class StudentUpdateFailedException extends  RuntimeException {
    public StudentUpdateFailedException(String studentNumber) {
        super("Failed to update student with ID: " + studentNumber);
    }
}
