package com.emis.studentsservice.exception;

import java.util.concurrent.TimeoutException;

public class StudentsServiceTimeoutException extends Throwable {
  public StudentsServiceTimeoutException(String databaseTimeout, TimeoutException ex) {}
}
