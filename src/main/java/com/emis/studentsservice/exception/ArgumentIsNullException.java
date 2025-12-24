package com.emis.studentsservice.exception;

public class ArgumentIsNullException extends  RuntimeException {
    public ArgumentIsNullException(String argumentName) {
        super("Argument '" + argumentName + "' is null.");
    }
}
