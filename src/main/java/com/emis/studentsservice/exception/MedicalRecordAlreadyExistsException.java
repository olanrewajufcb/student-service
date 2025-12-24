package com.emis.studentsservice.exception;

public class MedicalRecordAlreadyExistsException extends RuntimeException {
    public MedicalRecordAlreadyExistsException(Long studentId) {
        super("Medical record already exists for student: " + studentId);
    }
}