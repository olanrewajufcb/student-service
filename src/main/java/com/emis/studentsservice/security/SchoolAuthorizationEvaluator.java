package com.emis.studentsservice.security;

import com.emis.studentsservice.enums.ResourceAction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component("schoolAuth")
@RequiredArgsConstructor
public class SchoolAuthorizationEvaluator {

    private final AuthorizationPolicy policy;

    public boolean authorize(
            Authentication authentication,
            String schoolCode,
            ResourceAction action
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        ActorContext ctx = ActorContextFactory.from(jwt);

        return policy.isAuthorized(ctx, schoolCode, action);
    }
}