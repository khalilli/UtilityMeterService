package com.example.utilitymeterservice.controller.api;

import com.example.utilitymeterservice.dto.request.CreateMeterReadingRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterReadingRequest;
import com.example.utilitymeterservice.dto.response.MeterReadingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Meter Readings", description = "Meter readings management APIs")
public interface MeterReadingApi {
    @Operation(
            summary = "Add a new meter reading",
            description = "Adds a new reading for a specific meter owned by the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Meter reading successfully created",
            content = @Content(schema = @Schema(implementation = MeterReadingResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    ResponseEntity<MeterReadingResponse> createReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @Valid
            @RequestBody(
                    description = "Meter reading creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateMeterReadingRequest.class))
            )
            CreateMeterReadingRequest createMeterReadingRequest
    );

    @Operation(
            summary = "Get all readings for a meter",
            description = "Returns paginated readings for a specific meter owned by the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Paginated list of meter readings",
            content = @Content(schema = @Schema(implementation = MeterReadingResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    ResponseEntity<Page<MeterReadingResponse>> getAllReadings(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @ParameterObject Pageable pageable
    );

    @Operation(
            summary = "Get meter reading by ID",
            description = "Returns a specific meter reading for a meter owned by the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Meter reading details",
            content = @Content(schema = @Schema(implementation = MeterReadingResponse.class))
    )
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter or reading not found")
    ResponseEntity<MeterReadingResponse> getReadingById(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    );

    @Operation(
            summary = "Update a meter reading",
            description = "Updates a meter reading (typically allowed only for the most recent reading)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Updated meter reading",
            content = @Content(schema = @Schema(implementation = MeterReadingResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid request or update not allowed")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter or reading not found")
    ResponseEntity<MeterReadingResponse> updateReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id,
            @Valid
            @RequestBody(
                    description = "Meter reading update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMeterReadingRequest.class))
            )
            UpdateMeterReadingRequest updateMeterReadingRequest
    );

    @Operation(
            summary = "Delete a meter reading",
            description = "Deletes a specific meter reading for a meter owned by the authenticated user"
    )
    @ApiResponse(responseCode = "204", description = "Meter reading successfully deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter or reading not found")
    ResponseEntity<Void> deleteReading(
            HttpServletRequest request,
            @PathVariable UUID meterId,
            @PathVariable UUID id
    );
}
