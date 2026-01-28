package com.example.utilitymeterservice.dto.request;

import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.model.enums.MeterType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateMeterRequest(
        @NotNull MeterType meterType,

        @NotNull String meterNumber,

        @Size(max = 20, message = "Unit of measurement must not exceed 20 characters")
        String unitOfMeasurement,

        @PastOrPresent(message = "Installation date cannot be in the future")
        LocalDate installationDate
) { }
