package com.example.utilitymeterservice.dto.response;

import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.model.enums.MeterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record MeterResponse(

        UUID id,

        MeterType meterType,

        String meterNumber,

        MeterStatus meterStatus,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate installationDate,

        String unitOfMeasurement,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate updatedAt
) {
}
