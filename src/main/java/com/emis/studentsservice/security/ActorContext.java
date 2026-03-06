package com.emis.studentsservice.security;


import java.util.Set;

import com.emis.studentsservice.enums.ActorType;
import com.emis.studentsservice.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActorContext {

  private final ActorType type;

  private final String username;
  private final String schoolCode;
  private final Set<UserRole> userRoles;
  private final String email;

  private final String serviceName;
  private final Set<String> serviceAuthorities;

  public boolean isUser() {
    return type == ActorType.USER;
  }

  public boolean isService() {
    return type == ActorType.SERVICE;
  }
}