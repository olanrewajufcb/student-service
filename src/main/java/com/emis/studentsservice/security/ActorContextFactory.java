package com.emis.studentsservice.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.emis.studentsservice.enums.UserRole;
import org.springframework.security.oauth2.jwt.Jwt;

public class ActorContextFactory {
    private ActorContextFactory() {
    }


    public static ActorContext from(Jwt jwt) {

        String clientId = jwt.getClaimAsString("client_id");
        String username = jwt.getClaimAsString("preferred_username");

        if (clientId != null && username == null) {
            return new ServiceActorContext(
                    clientId,
                    extractScopes(jwt),
                    extractAuthorities(jwt)
            );
        }

        return new UserActorContext(
                username,
                jwt.getClaimAsString("schoolCode"),
                extractUserRoles(jwt),
                jwt.getClaimAsString("lga"),
                jwt.getClaimAsString("state")
        );
    }

    private static Set<UserRole> extractUserRoles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) return Set.of();

        return roles.stream()
                .map(String::toUpperCase)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    private static Set<String> extractScopes(Jwt jwt) {
        List<String> scopes = jwt.getClaimAsStringList("scope");
        return scopes == null ? Set.of() : Set.copyOf(scopes);
    }

    private static Set<String> extractAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles == null ? Set.of() : Set.copyOf(roles);
    }
}