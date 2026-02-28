package com.emis.studentsservice.security;

import com.emis.studentsservice.enums.UserRole;

import java.util.Set;

public record UserActorContext(
        String username,
        String schoolCode,
        Set<UserRole> roles,
        String lga,
        String state
) implements ActorContext {

    @Override
    public boolean isUser() { return true; }

    @Override
    public boolean isService() { return false; }

    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }
}