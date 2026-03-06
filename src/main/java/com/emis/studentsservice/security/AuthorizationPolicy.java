package com.emis.studentsservice.security;


import com.emis.studentsservice.config.StudentConfigurationProperties;
import com.emis.studentsservice.enums.ResourceAction;
import com.emis.studentsservice.enums.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationPolicy {

    private final StudentConfigurationProperties properties;

    public Mono<Boolean> isAuthorized(
            ActorContext ctx,
            String schoolCode,
            ResourceAction action
    ) {

        if (ctx.isService()) {
            return Mono.just(authorizeService(ctx, action));
        }

        return Mono.just(authorizeUser(ctx, schoolCode, action));
    }

    private boolean authorizeUser(
            ActorContext ctx,
            String schoolCode,
            ResourceAction action
    ) {
    // Auth check user=school2.admin, roles=[SCHOOL_ADMIN], schoolCodeHeader=TT1273,
    // tokenSchoolCode=SCH-001
    log.info(
        "Auth check user={}, roles={}, schoolCodeHeader={}, tokenSchoolCode={}",
        ctx.getUsername(),
        ctx.getUserRoles(),
        schoolCode,
        ctx.getSchoolCode());

        if (!hasSchoolScope(ctx, schoolCode)) {
            return false;
        }

        StudentConfigurationProperties.ActionPolicy policy =
                properties.getActions().get(action);

        if (policy == null) {
            return false;
        }

        return ctx.getUserRoles()
                .stream()
                .anyMatch(policy.getRoles()::contains);
    }

    private boolean hasSchoolScope(ActorContext ctx, String schoolCode) {

        if (ctx.getUserRoles().contains(UserRole.SYSTEM_ADMIN)) return true;
        if (ctx.getUserRoles().contains(UserRole.STATE_ADMIN))  return true;
        if (ctx.getUserRoles().contains(UserRole.LGA_ADMIN))    return true;

        return Objects.equals(schoolCode, ctx.getSchoolCode());
    }

    private boolean authorizeService(
            ActorContext ctx,
            ResourceAction action
    ) {

        StudentConfigurationProperties.ActionPolicy policy =
                properties.getActions().get(action);

        if (policy == null) {
            return false;
        }

        return ctx.getServiceAuthorities()
                .stream()
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