package com.example.utilitymeterservice.exceptions;

public class MeterNotFoundException extends RuntimeException {
    public MeterNotFoundException(String message) {
        super(message);
    }

    public MeterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}