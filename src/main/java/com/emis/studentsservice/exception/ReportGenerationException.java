package com.emis.studentsservice.exception;

public class ReportGenerationException extends RuntimeException {
  public ReportGenerationException(String msg, Throwable ex) {
    super(msg, ex);
  }
}