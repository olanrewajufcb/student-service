package com.emis.studentsservice.domain.db;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("enrolment_history")
public class EnrollmentHistory{
        @Id private Long enrollmentId;
                          private Long studentId;
                            private String schoolYear;
                              private String gradeLevel;
                               private String schoolName;
                              private Long schoolId;
                              private String type;
                               private String note;
                             private Boolean isDeleted;
                              private LocalDateTime deletedAt;
                              private LocalDateTime effectiveDate;
                             private LocalDateTime recordedAt;
}
