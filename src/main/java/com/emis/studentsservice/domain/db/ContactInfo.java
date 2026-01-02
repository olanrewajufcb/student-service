package com.emis.studentsservice.domain.db;



public record ContactInfo(String email,
                          String phone,
                          String address1,
                          String address2,
                          String city,
                          String lga,
                          String state,
                          String postalCode) {}
