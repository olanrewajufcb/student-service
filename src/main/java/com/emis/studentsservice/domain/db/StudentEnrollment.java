package com.emis.studentsservice.domain.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "student_enrollments",  schema = "student_schema")
@Builder
@Getter
@Setter
public class StudentEnrollment {
  @Id
  private Long enrollmentId;
  private Long studentId;
  private String studentNumber;
  private Long schoolId;
  private String schoolCode;
  private Long classId;
  private String className;
  private String academicYear;
  private String enrollmentType;
  private String enrollmentStatus;
  private LocalDate enrollmentDate;
  private LocalDate completionDate;
  private String progressionStatus;
  private LocalDate exitDate;
  private String exitReason;
  private String dropoutReason;
  private LocalDate dropoutDate;
  private String remarks;
  private String promotedFromClass;
  private String promotedToClass;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
}