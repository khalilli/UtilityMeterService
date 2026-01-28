package com.example.utilitymeterservice.service.impl;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.exceptions.DuplicateMeterNumberException;
import com.example.utilitymeterservice.exceptions.UnauthorizedException;
import com.example.utilitymeterservice.mapper.MeterMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.User;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.repository.UserRepository;
import com.example.utilitymeterservice.service.MeterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;
    private final UserRepository userRepository;
    private final MeterMapper meterMapper;

    @Override
    @Transactional
    public MeterResponse createMeter(CreateMeterRequest request, UUID userId) {
        log.info("Creating meter for user: {}", userId);

        // Validate meter number is unique
        if (meterRepository.existsByMeterNumber(request.meterNumber())) {
            log.warn("Meter number already exists: {}", request.meterNumber());
            throw new DuplicateMeterNumberException("Meter number already exists: " + request.meterNumber());
        }

        // Get user
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Create and save meter
        Meter meter = meterMapper.toEntity(request, new User());
        Meter savedMeter = meterRepository.save(meter);

        log.info("Meter created successfully with ID: {}", savedMeter.getId());
        return meterMapper.toResponse(savedMeter);
    }
}
