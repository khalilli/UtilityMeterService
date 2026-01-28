package com.example.utilitymeterservice.controller.api;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.UUID;

@Tag(name = "Meters", description = "Meter management APIs")
public interface MeterApi {
    @Operation(
            summary = "Create a new meter",
            description = "Creates a meter and assigns it to a user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Meter successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MeterResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request"
    )
    @PostMapping
    ResponseEntity<MeterResponse> createMeter(
            @Valid
            @RequestBody(
                    description = "Meter creation request",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateMeterRequest.class)
                    )
            )
            CreateMeterRequest request,
            @RequestHeader UUID userId
    );
}
