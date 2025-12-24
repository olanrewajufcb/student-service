package com.emis.studentsservice.dto.response;

import java.util.List;

public record MedicalRecordResponse(Long recordId, Long studentId, String bloodType,
                                    List<String> allergies, List<String> chronicConditions,
                                    String notes) {}
