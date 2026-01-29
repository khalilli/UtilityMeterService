package com.example.utilitymeterservice.service;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.exceptions.InactiveMeterException;
import com.example.utilitymeterservice.exceptions.MeterNotFoundException;
import com.example.utilitymeterservice.mapper.MeterReadingMapper;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.MeterReading;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import com.example.utilitymeterservice.repository.MeterReadingRepository;
import com.example.utilitymeterservice.repository.MeterRepository;
import com.example.utilitymeterservice.service.impl.MeterReadingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MeterReadingServiceImplTest {

    private MeterRepository meterRepository;
    private MeterReadingRepository readingRepository;
    private MeterReadingMapper mapper;
    private MeterReadingServiceImpl service;

    @BeforeEach
    void setup() {
        meterRepository = Mockito.mock(MeterRepository.class);
        readingRepository = Mockito.mock(MeterReadingRepository.class);
        mapper = Mockito.mock(MeterReadingMapper.class);
        service = new MeterReadingServiceImpl(meterRepository, readingRepository, mapper);
    }

    @Test
    void createReading_success() {
        UUID meterId = UUID.randomUUID();
        String userId = "user-1";

        Meter meter = new Meter();
        meter.setId(meterId);
        meter.setMeterStatus(MeterStatus.ACTIVE);

        when(meterRepository.findByIdAndUserId(meterId, userId)).thenReturn(Optional.of(meter));

        CreateMeterReadingRequest req = new CreateMeterReadingRequest(new BigDecimal("123.45"), LocalDate.now());
        MeterReading entity = new MeterReading();
        entity.setMeter(meter);

        when(mapper.toEntity(req, meter)).thenReturn(entity);

        MeterReading saved = new MeterReading();
        saved.setId(UUID.randomUUID());
        saved.setMeter(meter);
        saved.setReadingValue(new BigDecimal("123.45"));

        when(readingRepository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(
                new MeterReadingResponse(saved.getId(),
                        saved.getReadingValue(),
                        BigDecimal.ONE,
                        saved.getReadingDate(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                        ));

        MeterReadingResponse resp = service.createReading(meterId, req, userId);

        assertThat(resp).isNotNull();
        assertThat(resp.readingValue()).isEqualTo(new BigDecimal("123.45"));

        verify(meterRepository).findByIdAndUserId(meterId, userId);
        verify(readingRepository).save(entity);
    }

    @Test
    void createReading_meterNotFound_throws() {
        UUID meterId = UUID.randomUUID();
        String userId = "user-1";

        when(meterRepository.findByIdAndUserId(meterId, userId)).thenReturn(Optional.empty());

        CreateMeterReadingRequest req = new CreateMeterReadingRequest(new BigDecimal("123.45"), LocalDate.now());

        assertThatThrownBy(() -> service.createReading(meterId, req, userId))
                .isInstanceOf(MeterNotFoundException.class);
    }

    @Test
    void createReading_inactiveMeter_throws() {
        UUID meterId = UUID.randomUUID();
        String userId = "user-1";

        Meter meter = new Meter();
        meter.setId(meterId);
        meter.setMeterStatus(MeterStatus.INACTIVE);

        when(meterRepository.findByIdAndUserId(meterId, userId)).thenReturn(Optional.of(meter));

        CreateMeterReadingRequest req = new CreateMeterReadingRequest(new BigDecimal("123.45"), LocalDate.now());

        assertThatThrownBy(() -> service.createReading(meterId, req, userId))
                .isInstanceOf(InactiveMeterException.class);
    }
}