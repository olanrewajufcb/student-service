package com.emis.studentsservice.dto.response;

import com.emis.studentsservice.enums.StudentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StudentResponse(
         Long studentId,
         Long schoolId,
         String studentNumber,
         String firstName,
         String lastName,
         String fullName,
         LocalDate dateOfBirth,
         String gender,
         String schoolName,
         String schoolCode,
         LocalDateTime enrollmentDate,
         String gradeLevel,
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
