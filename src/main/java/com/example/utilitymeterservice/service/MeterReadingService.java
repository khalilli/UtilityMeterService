package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MeterReadingService {

    MeterReadingResponse createReading(UUID meterId, CreateMeterReadingRequest request, String userId);

    Page<MeterReadingResponse> getAllReadings(UUID meterId, String userId, Pageable pageable);

    MeterReadingResponse getReadingById(UUID meterId, UUID readingId, String userId);

    MeterReadingResponse updateReading(UUID meterId, UUID readingId, UpdateMeterReadingRequest request, String userId);

    void deleteReading(UUID meterId, UUID readingId, String userId);
}
