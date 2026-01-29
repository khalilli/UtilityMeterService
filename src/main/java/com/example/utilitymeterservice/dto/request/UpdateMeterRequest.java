package com.example.utilitymeterservice.dto.request;

import com.example.utilitymeterservice.model.enums.MeterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateMeterRequest(
        @NotNull(message = "Meter type is required")
        MeterType meterType,

        @NotBlank(message = "Meter number is required")
        @Size(min = 5, max = 50, message = "Meter number must be between 5 and 50 characters")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Meter number must contain only uppercase letters, numbers, and hyphens")
        String meterNumber,

        @Size(max = 20, message = "Unit of measurement must not exceed 20 characters")
        String unitOfMeasurement,

        @NotNull(message = "Installation date is required")
        @PastOrPresent(message = "Installation date cannot be in the future")
        LocalDate installationDate
) { }