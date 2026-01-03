package com.example.doctorservice.exception;

public class DuplicateDepartmentException extends RuntimeException {
    public DuplicateDepartmentException(String message) {
        super(message);
    }

    public DuplicateDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }
}