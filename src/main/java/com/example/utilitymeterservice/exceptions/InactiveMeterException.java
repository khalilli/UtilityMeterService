package com.example.utilitymeterservice.exceptions;

public class InactiveMeterException extends RuntimeException {
    public InactiveMeterException(String message) {
        super(message);
    }

    public InactiveMeterException(String message, Throwable cause) {
        super(message, cause);
    }
}