package com.example.utilitymeterservice.mapper;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.model.entity.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MeterMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "meterStatus", constant = "ACTIVE")
    Meter toEntity(CreateMeterRequest request, String userId);

    MeterResponse toResponse(Meter meter);

    @Named("updateMeter")
    void updateMeterFromRequest(UpdateMeterRequest request, @MappingTarget Meter meter);
}
