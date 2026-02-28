package com.emis.studentsservice.config;


import com.emis.studentsservice.enums.ResourceAction;
import com.emis.studentsservice.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "emis.services")
public class StudentConfigurationProperties {

    @NestedConfigurationProperty
    private SchoolServiceProperties configuration;

    private String storageBaseUrl;
    private int timeout;



    @Getter
    @Setter
    public static class SchoolServiceProperties {
        private String baseUrl;
        private String getSchoolDetailsUrl;
        private String validateSchoolExistsUrl;


    }


    @NotNull
    private Map<ResourceAction, ActionPolicy> actions = new EnumMap<>(ResourceAction.class);


    @Getter
    @Setter
    public static class ActionPolicy {
        private final Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
        private final Set<String> serviceAuthorities = new HashSet<>();
    }

}
