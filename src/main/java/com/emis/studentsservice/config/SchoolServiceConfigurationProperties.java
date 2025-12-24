package com.emis.studentsservice.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "emis.services")
public class SchoolServiceConfigurationProperties {

    @NestedConfigurationProperty
    private SchoolServiceProperties configuration;


    @Getter
    @Setter
    public static class SchoolServiceProperties {
        private String baseUrl;
        private String getSchoolDetailsUrl;
        private String validateSchoolExistsUrl;


    }

}
