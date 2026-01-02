package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.DisabilityLevel;
import com.emis.studentsservice.enums.DisabilityType;

public record MedicalRecordRequest(String bloodType, String allergies,
                                   String chronicConditions, String notes,
                                   DisabilityType disabilityType, DisabilityLevel disabilityLevel) {}
