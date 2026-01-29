package com.example.utilitymeterservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UpdateMeterReadingRequest (
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal readingValue,

        LocalDate readingDate
){ }
