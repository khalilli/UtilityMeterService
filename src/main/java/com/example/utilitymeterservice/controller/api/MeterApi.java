package com.example.utilitymeterservice.controller.api;

import com.example.utilitymeterservice.dto.request.CreateMeterRequest;
import com.example.utilitymeterservice.dto.request.UpdateMeterRequest;
import com.example.utilitymeterservice.dto.response.MeterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;
import java.util.UUID;

@Tag(name = "Meters", description = "Meter management APIs")
public interface MeterApi {
    @Operation(
            summary = "Create a new meter",
            description = "Creates a meter and assigns it to a user"
    )
    @ApiResponse(responseCode = "200", description = "Meter successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeterResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @PostMapping
    ResponseEntity<MeterResponse> createMeter(
            HttpServletRequest request,
            @Valid
            @RequestBody(description = "Meter creation request", required = true,
                    content = @Content(schema = @Schema(implementation = CreateMeterRequest.class)))
            CreateMeterRequest createMeterRequest
    );

    @Operation(
            summary = "Get all meters",
            description = "Returns a list of all meters for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "List of meters",
            content = @Content(schema = @Schema(implementation = MeterResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    ResponseEntity<List<MeterResponse>> getAllMeters(HttpServletRequest request);

    @Operation(
            summary = "Get meter by ID",
            description = "Returns the details of a specific meter for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Meter details",
            content = @Content(schema = @Schema(implementation = MeterResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    ResponseEntity<MeterResponse> getMeterById(HttpServletRequest request, @PathVariable UUID id);

    @Operation(
            summary = "Update a meter",
            description = "Updates the details of a meter for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Updated meter details",
            content = @Content(schema = @Schema(implementation = MeterResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    ResponseEntity<MeterResponse> updateMeter(
            HttpServletRequest request,
            @PathVariable UUID id,
            @Valid
            @RequestBody(description = "Meter update request", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateMeterRequest.class)))
            UpdateMeterRequest updateMeterRequest
    );

    @Operation(
            summary = "Deactivate a meter",
            description = "Deactivates a meter (sets status to INACTIVE) for the authenticated user"
    )
    @ApiResponse(responseCode = "204", description = "Meter successfully deactivated")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    ResponseEntity<Void> deactivateMeter(HttpServletRequest request, @PathVariable UUID id);
}
