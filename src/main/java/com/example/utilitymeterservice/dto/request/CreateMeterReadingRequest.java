package com.example.utilitymeterservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateMeterReadingRequest(
//        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal readingValue,

        LocalDate readingDate
) {
}
