package com.emis.studentsservice.exception;

import java.util.concurrent.TimeoutException;

public class StudentServiceTimeoutException extends Throwable {
public StudentServiceTimeoutException(String databaseTimeout, TimeoutException ex) {
    super(databaseTimeout, ex);
}}
