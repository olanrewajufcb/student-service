package com.emis.studentsservice.dto;

public record ContactInfoRequest( String email,
                                  String phone,
                                  String address1,
                                  String address2,
                                  String city,
                                  String lga,
                                  String state,
                                  String postalCode,
                                  String country) {}
