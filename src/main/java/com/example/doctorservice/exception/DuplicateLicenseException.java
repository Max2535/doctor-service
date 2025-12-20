package com.example.doctorservice.exception;

public class DuplicateLicenseException extends RuntimeException {
    public DuplicateLicenseException(String message) {
        super(message);
    }
}