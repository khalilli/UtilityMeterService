package com.example.utilitymeterservice.controller;

import com.example.utilitymeterservice.controller.api.MeterApi;
import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.service.MeterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
@Slf4j
public class MeterController implements MeterApi {

    private final MeterService meterService;

    @Override
    public ResponseEntity<MeterResponse> createMeter(@Valid @RequestBody CreateMeterRequest request, @RequestHeader UUID userId) {
        return ResponseEntity.ok(meterService.createMeter(request, userId));
    }
}
