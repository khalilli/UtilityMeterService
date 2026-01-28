package com.example.utilitymeterservice.mapper;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterDetailResponse;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.MeterReading;
import com.example.utilitymeterservice.model.entity.User;
import com.example.utilitymeterservice.model.enums.MeterStatus;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeterMapper {
    public Meter toEntity(CreateMeterRequest request, User user) {
        Meter meter = new Meter();
        meter.setUser(user);
        meter.setMeterType(request.meterType());
        meter.setMeterNumber(request.meterNumber());
        meter.setInstallationDate(request.installationDate());
//        meter.setLocation(request.getLocation());
        meter.setUnitOfMeasurement(request.unitOfMeasurement());
        meter.setMeterStatus(MeterStatus.ACTIVE);
        return meter;
    }

    public void updateEntity(Meter meter, UpdateMeterRequest request) {
        if (request.meterStatus() != null) {
            meter.setMeterStatus(request.meterStatus());
        }
        if (request.installationDate() != null) {
            meter.setInstallationDate(request.installationDate());
        }
//        if (request.getLocation() != null) {
//            meter.setLocation(request.getLocation());
//        }
        if (request.unitOfMeasurement() != null) {
            meter.setUnitOfMeasurement(request.unitOfMeasurement());
        }
    }

    public MeterResponse toResponse(Meter meter) {
        return MeterResponse.builder()
                .id(meter.getId())
                .meterType(meter.getMeterType())
                .meterNumber(meter.getMeterNumber())
                .installationDate(meter.getInstallationDate())
                .meterStatus(meter.getMeterStatus())
//                .location(meter.getLocation())
                .unitOfMeasurement(meter.getUnitOfMeasurement())
                .createdAt(meter.getCreatedAt())
                .updatedAt(meter.getUpdatedAt())
                .build();
    }

//    public MeterDetailResponse toDetailResponse(Meter meter) {
//        MeterDetailResponse response = MeterDetailResponse.builder()
//                .id(meter.getId())
//                .meterType(meter.getMeterType())
//                .meterNumber(meter.getMeterNumber())
//                .installationDate(meter.getInstallationDate())
//                .meterStatus(meter.getMeterStatus())
////                .location(meter.getLocation())
//                .unitOfMeasurement(meter.getUnitOfMeasurement())
//                .createdAt(meter.getCreatedAt())
//                .updatedAt(meter.getUpdatedAt())
//                .totalReadingsCount(meter.getMeterReadings() != null ? meter.getMeterReadings().size() : 0)
//                .build();
//
//        // Set latest reading if available
//        if (meter.getMeterReadings() != null && !meter.getMeterReadings().isEmpty()) {
//            MeterReading latestReading = meter.getMeterReadings().stream()
//                    .max(Comparator.comparing(MeterReading::getReadingDate))
//                    .orElse(null);
//
////            if (latestReading != null) {
////                // You'll need to inject MeterReadingMapper or create inline
////                response.setLatestReading(toMeterReadingResponse(latestReading));
////            }
//
//            // Calculate total consumption
//            Double totalConsumption = meter.getMeterReadings().stream()
//                    .map(MeterReading::getConsumption)
//                    .filter(c -> c != null)
//                    .reduce(0.0, Double::sum);
//            response.totalConsumption(totalConsumption);
//        }
//
//        return response;
//    }
//
//    public List<MeterResponse> toResponseList(List<Meter> meters) {
//        return meters.stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }

    // Helper method - you'll move this to MeterReadingMapper later
//    private MeterReadingResponse toMeterReadingResponse(MeterReading reading) {
//        return MeterReadingResponse.builder()
//                .id(reading.getId())
//                .readingValue(reading.getReadingValue())
//                .readingDate(reading.getReadingDate())
//                .consumption(reading.getConsumption())
//                .createdAt(reading.getCreatedAt())
//                .build();
//    }
}
