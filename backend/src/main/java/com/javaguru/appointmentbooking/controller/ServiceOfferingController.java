package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.ServiceOfferingRequest;
import com.javaguru.appointmentbooking.dto.response.PagedResponse;
import com.javaguru.appointmentbooking.dto.response.ServiceOfferingResponse;
import com.javaguru.appointmentbooking.service.ServiceOfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Service Offerings",
        description = """
                Service offering management endpoints used
                to create, retrieve and deactivate loan
                and banking services available for appointment booking.
                """
)
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService
            serviceOfferingService;

    @Operation(
            summary = "Create Service Offering",
            description = """
                Creates a new service offering that customers
                can select during appointment booking.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Service offering created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Service offering already exists"
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('SERVICE_CREATE')")
    public ResponseEntity<ServiceOfferingResponse>
    createService(@Valid @RequestBody ServiceOfferingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceOfferingService.createService(request));
    }

    @Operation(
            summary = "Get Service Offerings",
            description = """
                Retrieves all active service offerings
                available for appointment booking.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Service offerings retrieved successfully"
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    public ResponseEntity<PagedResponse<ServiceOfferingResponse>> getServices(Pageable pageable) {

        return ResponseEntity.ok(serviceOfferingService.getServices(pageable));
    }

    @Operation(
            summary = "Deactivate Service Offering",
            description = """
                Marks a service offering as inactive.
                Inactive services can no longer be selected
                when booking appointments.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Service offering deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service offering not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('SERVICE_DELETE')")
    public ResponseEntity<Void>
    deactivateService(

            @Parameter(
                    description = "Service offering identifier",
                    required = true
            )@PathVariable UUID serviceId) {

        serviceOfferingService.deactivateService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
