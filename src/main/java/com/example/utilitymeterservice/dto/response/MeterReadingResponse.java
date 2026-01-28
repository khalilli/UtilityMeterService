package com.example.utilitymeterservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MeterReadingResponse(
        UUID id,
        BigDecimal readingValue,
        BigDecimal consumption,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate readingDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) { }
