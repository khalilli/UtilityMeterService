package com.example.utilitymeterservice.dto.request;

import com.example.utilitymeterservice.model.enums.MeterStatus;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateMeterRequest(

        MeterStatus meterStatus,

        @PastOrPresent(message = "Installation date cannot be in the future")
        LocalDate installationDate,

        @Size(max = 20, message = "Unit of measurement must not exceed 20 characters")
        String unitOfMeasurement
) { }
