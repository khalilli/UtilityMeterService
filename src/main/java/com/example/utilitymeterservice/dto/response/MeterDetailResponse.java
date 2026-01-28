package com.example.utilitymeterservice.dto.response;

import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.model.enums.MeterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MeterDetailResponse (
        UUID id,

        MeterType meterType,

        String meterNumber,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate installationDate,

        MeterStatus meterStatus,

        String location,

        String unitOfMeasurement,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,

//        MeterReadingResponse latestReading;

        Integer totalReadingsCount,

        Double totalConsumption
) { }
