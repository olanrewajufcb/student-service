package com.emis.studentsservice.exception;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {


        private final String fieldName;
        private final Object rejectedValue;

        public AlreadyExistException(String message) {
            super(message);
            this.fieldName = null;
            this.rejectedValue = null;
        }

        public AlreadyExistException(String message, String fieldName, Object rejectedValue) {
            super(message);
            this.fieldName = fieldName;
            this.rejectedValue = rejectedValue;
        }
}
