package com.example.utilitymeterservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMeterReadingRequest(
        @NotNull(message = "Reading value is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Reading value must be non-negative")
        BigDecimal readingValue,

        @NotNull(message = "Reading date is required")
        LocalDate readingDate
) {
}