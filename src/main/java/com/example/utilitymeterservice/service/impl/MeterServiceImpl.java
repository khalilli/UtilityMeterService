package com.example.utilitymeterservice.service.impl;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.exceptions.DuplicateMeterNumberException;
import com.example.utilitymeterservice.mapper.MeterMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.service.MeterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;
    private final MeterMapper meterMapper;

    @Override
    @Transactional
    public MeterResponse createMeter(CreateMeterRequest request, String userId) {
        log.info("Creating meter for user: {}", userId);

        if (meterRepository.existsByMeterNumber(request.meterNumber())) {
            log.warn("Meter number already exists: {}", request.meterNumber());
            throw new DuplicateMeterNumberException("Meter number already exists: " + request.meterNumber());
        }

        Meter meter = meterMapper.toEntity(request, userId);
        Meter savedMeter = meterRepository.saveAndFlush(meter);

        log.info("Meter created successfully with ID: {}", savedMeter.getId());
        return meterMapper.toResponse(savedMeter);
    }

    @Override
    @Transactional
    public List<MeterResponse> getAllMeters(String userId) {
        return meterRepository.findAllByUserId(userId)
                .stream()
                .map(meterMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MeterResponse getMeterById(UUID meterId, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found"));
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public MeterResponse updateMeter(UUID meterId, UpdateMeterRequest request, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        meterMapper.updateMeterFromRequest(request, meter);
        Meter updated = meterRepository.save(meter);

        return meterMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deactivateMeter(UUID meterId, String userId) {
        Meter meter = meterRepository.findByIdAndUserId(meterId, userId)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        meter.setMeterStatus(MeterStatus.INACTIVE);
        meterRepository.save(meter);
    }
}
