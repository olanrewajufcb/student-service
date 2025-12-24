package com.emis.studentsservice.dto.response;

public record GuardianResponse(
         Long guardianId,
         Long studentId,
         String firstName,
         String lastName,
         String relationship,
         String email,
         String phone,
         String address,
         Boolean isPrimaryContact
) {}
