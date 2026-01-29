package com.example.utilitymeterservice.dto;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
    public ApiError(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message);
    }
}

