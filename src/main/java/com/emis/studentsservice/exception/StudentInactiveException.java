package com.emis.studentsservice.exception;

public class StudentInactiveException extends RuntimeException{
  public StudentInactiveException(String msg) {
    super(msg);
  }
}
