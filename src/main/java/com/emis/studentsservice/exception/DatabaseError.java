package com.emis.studentsservice.exception;

public class DatabaseError extends  RuntimeException {
  public DatabaseError(String msg) {
      super(msg);
  }
}
