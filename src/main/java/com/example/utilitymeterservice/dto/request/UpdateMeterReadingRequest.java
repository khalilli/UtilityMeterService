package com.example.utilitymeterservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UpdateMeterReadingRequest (
        @NotNull
        @Min(0)
        BigDecimal readingValue,

        LocalDate readingDate
){ }
