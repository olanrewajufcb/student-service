package com.emis.studentsservice.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDropoutRiskResponse {
    Long studentId;
    String studentNumber;
    String riskLevel;
    Integer absentDays;
    Integer attendanceRate;
    Integer riskScore;
}
