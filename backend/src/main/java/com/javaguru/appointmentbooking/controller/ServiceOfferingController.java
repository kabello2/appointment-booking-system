package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.ServiceOfferingRequest;
import com.javaguru.appointmentbooking.dto.response.ServiceOfferingResponse;
import com.javaguru.appointmentbooking.service.ServiceOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService
            serviceOfferingService;

    @PostMapping
    @PreAuthorize("hasAuthority('SERVICE_CREATE')")
    public ResponseEntity<ServiceOfferingResponse>
    createService(@Valid @RequestBody ServiceOfferingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceOfferingService.createService(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SERVICE_VIEW')")
    public ResponseEntity<List<ServiceOfferingResponse>> getServices() {

        return ResponseEntity.ok(serviceOfferingService.getServices());
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('SERVICE_DELETE')")
    public ResponseEntity<Void>
    deactivateService(@PathVariable UUID serviceId) {

        serviceOfferingService.deactivateService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
