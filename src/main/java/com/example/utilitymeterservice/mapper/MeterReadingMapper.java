package com.example.utilitymeterservice.mapper;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.model.entity.Meter;
import com.example.utilitymeterservice.model.entity.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MeterReadingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meter", source = "meter")
    @Mapping(target = "consumption", ignore = true)
    MeterReading toEntity(CreateMeterReadingRequest request, Meter meter);

    MeterReadingResponse toResponse(MeterReading meterReading);

    @Named("updateMeterReading")
    void updateFromRequest(UpdateMeterReadingRequest request, @MappingTarget MeterReading meterReading);
}
