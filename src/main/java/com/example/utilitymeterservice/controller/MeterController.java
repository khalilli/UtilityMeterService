package com.example.utilitymeterservice.controller;

import com.example.utilitymeterservice.controller.api.MeterApi;
import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import com.example.utilitymeterservice.service.MeterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
