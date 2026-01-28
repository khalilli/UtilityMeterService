package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;

import java.util.List;
import java.util.UUID;

public interface MeterService {

    MeterResponse createMeter(CreateMeterRequest request, String userId);

    List<MeterResponse> getAllMeters(String userId);

    MeterResponse getMeterById(UUID meterId, String userId);

    MeterResponse updateMeter(UUID meterId, UpdateMeterRequest request, String userId);

    void deactivateMeter(UUID meterId, String userId);
}
