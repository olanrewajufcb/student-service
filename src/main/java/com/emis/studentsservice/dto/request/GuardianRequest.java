package com.emis.studentsservice.dto.request;

public record GuardianRequest(Long guardianId, String firstName, String lastName,
                              String relationship, String email, String phone, String address,
                              Boolean isPrimaryContact) {

}
