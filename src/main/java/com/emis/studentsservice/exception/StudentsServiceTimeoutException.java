package com.emis.studentsservice.exception;

import java.util.concurrent.TimeoutException;

public class StudentsServiceTimeoutException extends RuntimeException {
  public StudentsServiceTimeoutException(String databaseTimeout, Throwable ex) {}
}
