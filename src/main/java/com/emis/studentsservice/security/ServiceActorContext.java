package com.emis.studentsservice.security;

import java.util.Set;

public record ServiceActorContext(
        String clientId,
        Set<String> scopes,
        Set<String> authorities
) implements ActorContext {

    @Override
    public boolean isUser() { return false; }

    @Override
    public boolean isService() { return true; }

    public boolean hasAuthority(String authority) {
        return authorities.contains(authority);
    }
}