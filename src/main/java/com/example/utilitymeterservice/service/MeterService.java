package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;

import java.util.UUID;

public interface MeterService {

    MeterResponse createMeter(CreateMeterRequest request, UUID userId);
}
