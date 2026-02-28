package com.emis.studentsservice.security;

public sealed interface ActorContext permits UserActorContext, ServiceActorContext{

    boolean isUser();
    boolean isService();
}
