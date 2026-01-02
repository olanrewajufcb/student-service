package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.enums.SchoolLevel;
import com.emis.studentsservice.enums.SchoolStatus;
import com.emis.studentsservice.enums.SchoolType;
import java.time.LocalDateTime;

public record SchoolDetailsResponse(Long schoolId,
                                    String schoolCode,          // Unique identifier "SCH-001"
                                    String schoolName,
                                    SchoolType type,
                                    SchoolLevel level,
                                    String address,
                                    String phone,
                                    String email,
                                    String principalName,
                                    Integer maxStudentsPerClass,
                                    Long schoolCapacity,
                                    String academicCalendar,    // "FIRST TERM", "SECOND"
                                    String city,
                                    String lga,
                                    String state,
                                    SchoolStatus status,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt) {}
