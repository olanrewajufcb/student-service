package com.emis.studentsservice.dto.request;

import com.emis.studentsservice.enums.GradeLevel;
import com.emis.studentsservice.enums.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateStudentRequest(
                                   String studentNumber,
                                   String firstName,
                                   String lastName,
                                   LocalDate dateOfBirth,
                                   String gender,
                                   LocalDateTime enrollmentDate,
                                   GradeLevel gradeLevel,
                                   StudentStatus status,
                                   String email,
                                   String phone,
                                   String address1,
                                   String address2,
                                   String city,
                                   String lga,
                                   String state,
                                   String postalCode,
                                   String country,
                                   String photoUrl) {}


