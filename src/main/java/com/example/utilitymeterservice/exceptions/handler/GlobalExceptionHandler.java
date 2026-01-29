package com.example.utilitymeterservice.exceptions.handler;

import com.example.utilitymeterservice.dto.ApiError;
import com.example.utilitymeterservice.exceptions.DuplicateMeterNumberException;
import com.example.utilitymeterservice.exceptions.InactiveMeterException;
import com.example.utilitymeterservice.exceptions.InvalidTokenException;
import com.example.utilitymeterservice.exceptions.MeterNotFoundException;
import com.example.utilitymeterservice.exceptions.MeterReadingNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MeterNotFoundException.class)
    public ResponseEntity<ApiError> handleMeterNotFound(MeterNotFoundException ex) {
        log.warn("Meter not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MeterReadingNotFoundException.class)
    public ResponseEntity<ApiError> handleMeterReadingNotFound(MeterReadingNotFoundException ex) {
        log.warn("Meter reading not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateMeterNumberException.class)
    public ResponseEntity<ApiError> handleDuplicateMeter(DuplicateMeterNumberException ex) {
        log.warn("Duplicate meter number: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InactiveMeterException.class)
    public ResponseEntity<ApiError> handleInactiveMeter(InactiveMeterException ex) {
        log.warn("Invalid operation on inactive meter: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException ex) {
        log.warn("Invalid token: {}", ex.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Request validation failed: " + ex.getBindingResult().getFieldError()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message) {
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(apiError);
    }
}