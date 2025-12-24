package com.emis.studentsservice.domain.db;


import org.springframework.data.relational.core.mapping.Column;

public record ContactInfo(String email,
                          String phone,
                          String address1,
                          String address2,
                          String city,
                          String lga,
                          String state,
                          @Column("postal_code")
                          String postalCode,
                          String country
) {}
