package com.emis.studentsservice.security;


import com.emis.studentsservice.config.StudentConfigurationProperties;
import com.emis.studentsservice.enums.ResourceAction;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.emis.studentsservice.enums.UserRole.*;

@Component
@Slf4j
public class AuthorizationPolicy {

    private final StudentConfigurationProperties properties;

    public AuthorizationPolicy(StudentConfigurationProperties properties) {
        this.properties = properties;
    }


    public boolean isAuthorized(
            ActorContext ctx,
            String schoolCode,
            ResourceAction action
    ) {

        if (ctx.isService()) {
            return authorizeService((ServiceActorContext) ctx, action);
        }
        return authorizeUser((UserActorContext) ctx, schoolCode, action);
    }

    private boolean authorizeUser(
            UserActorContext ctx,
            String schoolCode,
            ResourceAction action
    ) {

        if (!hasSchoolScope(ctx, schoolCode)) {
            return false;
        }

        StudentConfigurationProperties.ActionPolicy policy =
                properties.getActions().get(action);

        return ctx.roles().stream()
                .anyMatch(policy.getRoles()::contains);
    }

    private boolean hasSchoolScope(UserActorContext ctx, String schoolCode) {

        if (ctx.hasRole(SYSTEM_ADMIN)) return true;
        if (ctx.hasRole(STATE_ADMIN))  return true;
        if (ctx.hasRole(LGA_ADMIN))    return true;

        return schoolCode.equals(ctx.schoolCode());
    }

    private boolean authorizeService(
            ServiceActorContext ctx,
            ResourceAction action
    ) {

        StudentConfigurationProperties.ActionPolicy policy =
                properties.getActions().get(action);


        if (policy.getServiceAuthorities() == null) {
            return false;
        }

        return ctx.authorities().stream()
                .anyMatch(policy.getServiceAuthorities()::contains);
    }

    @PostConstruct
    void validatePolicies() {
        for (ResourceAction action : ResourceAction.values()) {
            if (!properties.getActions().containsKey(action)) {
                log.error("Missing authorization policy for action: {}", action);
                throw new IllegalStateException(
                        "Missing authorization policy for action: " + action);
            }
        }
    }
}
