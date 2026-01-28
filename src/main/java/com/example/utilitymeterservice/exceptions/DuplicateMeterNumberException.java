package com.example.utilitymeterservice.exceptions;

public class DuplicateMeterNumberException extends RuntimeException {
    public DuplicateMeterNumberException(String message) {
        super(message);
    }
}
