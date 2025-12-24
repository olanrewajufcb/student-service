package com.emis.studentsservice.dto.request;

public record MedicalRecordRequest(String bloodType, String allergies,
                                   String chronicConditions, String notes) {}
