package com.example.utilitymeterservice.controller;

import com.example.utilitymeterservice.controller.api.MeterReadingApi;
import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.security.JwtAuthFilter;
import com.example.utilitymeterservice.security.JwtService;
import com.example.utilitymeterservice.service.MeterReadingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for meter readings operations.
 */
@RestController
@RequestMapping("/api/meters/{meterId}/readings")
@RequiredArgsConstructor
@Slf4j
public class MeterReadingController implements MeterReadingApi {

    private final MeterReadingService service;

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<MeterReadingResponse> createReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @RequestBody @Valid CreateMeterReadingRequest createRequest
    ) {
        log.debug("Creating reading for meter: {}", meterId);
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReading(meterId, createRequest, user.userId()));
    }

    @GetMapping
    public ResponseEntity<Page<MeterReadingResponse>> getAllReadings(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            Pageable pageable
    ) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        return ResponseEntity.ok(service.getAllReadings(meterId, user.userId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterReadingResponse> getReadingById(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    ) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        return ResponseEntity.ok(service.getReadingById(meterId, id, user.userId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterReadingResponse> updateReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMeterReadingRequest updateRequest
    ) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        return ResponseEntity.ok(service.updateReading(meterId, id, updateRequest, user.userId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    ) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        service.deleteReading(meterId, id, user.userId());
        return ResponseEntity.noContent().build();
    }
}