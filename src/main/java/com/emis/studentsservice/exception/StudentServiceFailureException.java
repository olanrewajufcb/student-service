package com.emis.studentsservice.exception;

public class StudentServiceFailureException extends Throwable {
  public StudentServiceFailureException(String failedToFetchAllStudents, Throwable error) {}
}
