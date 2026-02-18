package com.emis.studentsservice.event;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEvent {
    private Long attendanceId;
    private String studentNumber;
    private Long sectionId;
    private String schoolCode;
    private String attendanceStatus;
    private String notes;
    private LocalDate attendanceDate;
    private String correlationId;
}


