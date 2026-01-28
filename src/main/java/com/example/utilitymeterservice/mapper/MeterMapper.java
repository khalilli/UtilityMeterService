package com.example.utilitymeterservice.mapper;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import org.springframework.stereotype.Component;

@Component
public class MeterMapper {
    public Meter toEntity(CreateMeterRequest request, String userId) {
        Meter meter = new Meter();
        meter.setUserId(userId);
        meter.setMeterType(request.meterType());
        meter.setMeterNumber(request.meterNumber());
        meter.setInstallationDate(request.installationDate());
        meter.setUnitOfMeasurement(request.unitOfMeasurement());
        meter.setMeterStatus(MeterStatus.ACTIVE);
        return meter;
    }

    public MeterResponse toResponse(Meter meter) {
        return MeterResponse.builder()
                .id(meter.getId())
                .meterType(meter.getMeterType())
                .meterNumber(meter.getMeterNumber())
                .installationDate(meter.getInstallationDate())
                .meterStatus(meter.getMeterStatus())
                .unitOfMeasurement(meter.getUnitOfMeasurement())
                .createdAt(meter.getCreatedAt())
                .updatedAt(meter.getUpdatedAt())
                .build();
    }
}
