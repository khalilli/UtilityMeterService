package com.example.utilitymeterservice.controller;

import com.example.utilitymeterservice.controller.api.MeterApi;
import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.service.MeterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
@Slf4j
public class MeterController implements MeterApi {

    private final MeterService meterService;

    @Override
    @PostMapping
    public ResponseEntity<MeterResponse> createMeter(HttpServletRequest request, @Valid @RequestBody CreateMeterRequest createMeterRequest) {
        JwtUser user = (JwtUser) request.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(meterService.createMeter(createMeterRequest, user.userId()));
    }

    @GetMapping
    public ResponseEntity<List<MeterResponse>> getAllMeters(HttpServletRequest request) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(meterService.getAllMeters(user.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterResponse> getMeterById(HttpServletRequest request, @PathVariable UUID id) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(meterService.getMeterById(id, user.userId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterResponse> updateMeter(
            HttpServletRequest request,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMeterRequest updateMeterRequest) {

        JwtUser user = (JwtUser) request.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(meterService.updateMeter(id, updateMeterRequest, user.userId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMeter(HttpServletRequest request, @PathVariable UUID id) {
        JwtUser user = (JwtUser) request.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();

        meterService.deactivateMeter(id, user.userId());
        return ResponseEntity.noContent().build();
    }

}
