package com.emis.studentsservice.dto.request;

public record GuardianRequest(String firstName, String lastName,
                              String relationship, String email, String phone, String address,
                              Boolean isPrimaryContact) {

}
