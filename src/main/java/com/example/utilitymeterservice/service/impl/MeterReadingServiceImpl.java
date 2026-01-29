package com.example.utilitymeterservice.service.impl;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.mapper.MeterReadingMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.MeterReading;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.repository.MeterReadingRepository;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.service.MeterReadingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final MeterReadingMapper mapper;

    @Override
    @Transactional
    public MeterReadingResponse createReading(UUID meterId, CreateMeterReadingRequest request, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found or not yours"));
        if (meter.getMeterStatus() != MeterStatus.ACTIVE) {
            throw new RuntimeException("Cannot add readings to inactive meter");
        }

        MeterReading reading = mapper.toEntity(request, meter);
        MeterReading saved = readingRepository.save(reading);
        return mapper.toResponse(saved);
    }

    @Override
    public Page<MeterReadingResponse> getAllReadings(UUID meterId, String userId, Pageable pageable) {
        meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found or not yours"));

        return readingRepository.findAllByMeterId(meterId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public MeterReadingResponse getReadingById(UUID meterId, UUID readingId, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found or not yours"));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new RuntimeException("Reading not found"));

        return mapper.toResponse(reading);
    }

    @Override
    @Transactional
    public MeterReadingResponse updateReading(UUID meterId, UUID readingId, UpdateMeterReadingRequest request, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found or not yours"));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new RuntimeException("Reading not found"));

        mapper.updateFromRequest(request, reading);
        MeterReading updated = readingRepository.save(reading);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteReading(UUID meterId, UUID readingId, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found or not yours"));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new RuntimeException("Reading not found"));

        readingRepository.delete(reading);
    }
}
