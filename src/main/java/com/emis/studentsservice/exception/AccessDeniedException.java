package com.emis.studentsservice.exception;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String msg) {
    super(msg);
  }
}
