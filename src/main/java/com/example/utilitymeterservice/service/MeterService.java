package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;

public interface MeterService {

    MeterResponse createMeter(CreateMeterRequest request, String userId);
}
