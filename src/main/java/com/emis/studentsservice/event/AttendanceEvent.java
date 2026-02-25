package com.emis.studentsservice.event;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_attendance_projection")
public class AttendanceEvent {
    private Long attendanceId;
    private String studentNumber;
    private Long sectionId;
    private String schoolCode;
    private String attendanceStatus;
    private String notes;
    private LocalDate attendanceDate;
    private String correlationId;
    private Long termId;
    private LocalDate lessonDate;
}
