package com.example.utilitymeterservice.service.impl;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.exceptions.InactiveMeterException;
import com.example.utilitymeterservice.exceptions.MeterNotFoundException;
import com.example.utilitymeterservice.exceptions.MeterReadingNotFoundException;
import com.example.utilitymeterservice.mapper.MeterReadingMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.MeterReading;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.repository.MeterReadingRepository;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.service.MeterReadingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {
    private final MeterRepository meterRepository;
    private final MeterReadingRepository readingRepository;
    private final MeterReadingMapper mapper;

    @Override
    @Transactional
    public MeterReadingResponse createReading(UUID meterId, CreateMeterReadingRequest request, String userId) {
        log.info("Creating reading for meter {} and user: {}", meterId, userId);

        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with ID: " + meterId));

        if (meter.getMeterStatus() != MeterStatus.ACTIVE) {
            throw new InactiveMeterException("Cannot add readings to inactive meter with ID: " + meterId);
        }

        MeterReading reading = mapper.toEntity(request, meter);
        MeterReading saved = readingRepository.save(reading);

        log.info("Meter reading created successfully with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeterReadingResponse> getAllReadings(UUID meterId, String userId, Pageable pageable) {
        log.debug("Fetching readings for meter {} and user: {}", meterId, userId);

        meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with ID: " + meterId));

        return readingRepository.findAllByMeterId(meterId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MeterReadingResponse getReadingById(UUID meterId, UUID readingId, String userId) {
        log.debug("Fetching reading {} for meter {} and user: {}", readingId, meterId, userId);

        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with ID: " + meterId));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new MeterReadingNotFoundException("Reading not found with ID: " + readingId));

        return mapper.toResponse(reading);
    }

    @Override
    @Transactional
    public MeterReadingResponse updateReading(UUID meterId, UUID readingId, UpdateMeterReadingRequest request, String userId) {
        log.info("Updating reading {} for meter {} and user: {}", readingId, meterId, userId);

        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with ID: " + meterId));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new MeterReadingNotFoundException("Reading not found with ID: " + readingId));

        mapper.updateFromRequest(request, reading);
        MeterReading updated = readingRepository.save(reading);

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteReading(UUID meterId, UUID readingId, String userId) {
        log.info("Deleting reading {} for meter {} and user: {}", readingId, meterId, userId);

        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with ID: " + meterId));

        MeterReading reading = readingRepository.findById(readingId)
                .filter(r -> r.getMeter().getId().equals(meter.getId()))
                .orElseThrow(() -> new MeterReadingNotFoundException("Reading not found with ID: " + readingId));

        readingRepository.delete(reading);
    }
}