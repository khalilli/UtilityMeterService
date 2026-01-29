package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.exceptions.DuplicateMeterNumberException;
import com.example.utilitymeterservice.mapper.MeterMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.model.enums.MeterType;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.service.impl.MeterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MeterServiceImplTest {

    private MeterRepository meterRepository;
    private MeterMapper meterMapper;
    private MeterServiceImpl meterService;

    @BeforeEach
    void setup() {
        meterRepository = Mockito.mock(MeterRepository.class);
        meterMapper = Mockito.mock(MeterMapper.class);
        meterService = new MeterServiceImpl(meterRepository, meterMapper);
    }

    @Test
    void createMeter_success() {
        CreateMeterRequest request = new CreateMeterRequest(MeterType.ELECTRICITY, "ABC-12345", LocalDate.now(), "kWh");
        String userId = "user-1";

        when(meterRepository.existsByMeterNumber(request.meterNumber())).thenReturn(false);

        Meter mapped = new Meter();
        mapped.setMeterNumber(request.meterNumber());
        mapped.setUserId(userId);

        when(meterMapper.toEntity(request, userId)).thenReturn(mapped);

        Meter saved = new Meter();
        saved.setId(UUID.randomUUID());
        saved.setMeterNumber(request.meterNumber());
        saved.setUserId(userId);

        when(meterRepository.saveAndFlush(mapped)).thenReturn(saved);
        when(meterMapper.toResponse(saved)).thenReturn(
                new MeterResponse(
                        saved.getId(),
                        MeterType.ELECTRICITY,
                        request.meterNumber(),
                        MeterStatus.ACTIVE,
                        LocalDate.now(),
                        "kWh",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ));

        MeterResponse response = meterService.createMeter(request, userId);

        assertThat(response).isNotNull();
        assertThat(response.meterNumber()).isEqualTo(request.meterNumber());

        verify(meterRepository).existsByMeterNumber(request.meterNumber());
        verify(meterRepository).saveAndFlush(mapped);
    }

    @Test
    void createMeter_duplicateNumber_throws() {
        CreateMeterRequest request = new CreateMeterRequest(MeterType.ELECTRICITY, "ABC-12345", LocalDate.now(), "kWh");
        String userId = "user-1";

        when(meterRepository.existsByMeterNumber(request.meterNumber())).thenReturn(true);

        assertThatThrownBy(() -> meterService.createMeter(request, userId))
                .isInstanceOf(DuplicateMeterNumberException.class);

        verify(meterRepository).existsByMeterNumber(request.meterNumber());
        verify(meterRepository, never()).saveAndFlush(any());
    }

}