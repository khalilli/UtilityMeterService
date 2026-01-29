package com.example.utilitymeterservice.controller;

import com.example.utilitymeterservice.controller.api.MeterReadingApi;
import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import com.example.utilitymeterservice.service.MeterReadingService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/meters/{meterId}/readings")
@RequiredArgsConstructor
public class MeterReadingController implements MeterReadingApi {
    private final MeterReadingService service;

    @PostMapping
    public ResponseEntity<MeterReadingResponse> createReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @RequestBody @Valid CreateMeterReadingRequest createRequest
    ) {
        System.out.println("DTO = " + createRequest);
        JwtUser user = (JwtUser) request.getAttribute("user");
        return ResponseEntity.ok(service.createReading(meterId, createRequest, user.userId()));
    }

    @GetMapping
    public ResponseEntity<Page<MeterReadingResponse>> getAllReadings(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            Pageable pageable
    ) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        return ResponseEntity.ok(service.getAllReadings(meterId, user.userId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterReadingResponse> getReadingById(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    ) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        return ResponseEntity.ok(service.getReadingById(meterId, id, user.userId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterReadingResponse> updateReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id,
            @RequestBody UpdateMeterReadingRequest updateRequest
    ) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        return ResponseEntity.ok(service.updateReading(meterId, id, updateRequest, user.userId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    ) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        service.deleteReading(meterId, id, user.userId());
        return ResponseEntity.noContent().build();
    }
}
