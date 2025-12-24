package com.emis.studentsservice.exception;

public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(Long studentId) {
        super("Medical record not found for student: " + studentId);
    }
}