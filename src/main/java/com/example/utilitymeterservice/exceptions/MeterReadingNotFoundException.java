package com.example.utilitymeterservice.exceptions;

public class MeterReadingNotFoundException extends RuntimeException {
    public MeterReadingNotFoundException(String message) {
        super(message);
    }

    public MeterReadingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}