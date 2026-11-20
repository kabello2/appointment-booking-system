package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.BookAppointmentRequest;
import com.javaguru.appointmentbooking.dto.request.RescheduleAppointmentRequest;
import com.javaguru.appointmentbooking.dto.response.AppointmentResponse;
import com.javaguru.appointmentbooking.dto.response.AvailableSlotResponse;
import com.javaguru.appointmentbooking.dto.response.MyDashboardResponse;
import com.javaguru.appointmentbooking.entity.enums.AppointmentStatus;
import com.javaguru.appointmentbooking.service.AppointmentService;
import com.javaguru.appointmentbooking.service.AvailabilityService;
import com.javaguru.appointmentbooking.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Appointments",
        description = "Appointment Management APIs"
)
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserManagementService userManagementService;
    private final AvailabilityService availabilityService;

    @Operation(
            summary = "Book Appointment",
            description = "Creates a new appointment and automatically assigns a consultant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Branch, Service or Customer not found"
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_BOOK')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            Authentication authentication,
            @Valid @RequestBody BookAppointmentRequest request) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        AppointmentResponse response = appointmentService.bookAppointment(customerId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW_OWN')")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getMyAppointments(customerId));
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_VIEW','APPOINTMENT_VIEW_OWN')")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
    }

    @PutMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_BOOK','APPOINTMENT_VIEW')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }

    @PutMapping("/{appointmentId}/reschedule")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_BOOK','APPOINTMENT_VIEW')")
    public ResponseEntity<AppointmentResponse>
    rescheduleAppointment(
            @PathVariable UUID appointmentId,
            @Valid
            @RequestBody RescheduleAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService
                .rescheduleAppointment(appointmentId, request));
    }

    @PutMapping("/{appointmentId}/confirm")
    @PreAuthorize("hasAuthority('APPOINTMENT_CONFIRM')")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.confirmAppointment(appointmentId));
    }

    @PutMapping("/{appointmentId}/complete")
    @PreAuthorize("hasAuthority('APPOINTMENT_COMPLETE')")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.completeAppointment(appointmentId));
    }

    @GetMapping("/my/status")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointmentsByStatus(
            Authentication authentication,
            @RequestParam AppointmentStatus status) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(customerId, status));
    }

    @GetMapping("/my/date-range")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointmentsByDateRange(
            Authentication authentication,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(customerId, fromDate, toDate));
    }

    @GetMapping("/my/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(Authentication authentication) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(customerId));
    }

    @GetMapping("/consultant/my")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    public ResponseEntity<List<AppointmentResponse>> getConsultantAppointments(Authentication authentication) {

        UUID consultantId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getConsultantAppointments(consultantId));
    }

    @GetMapping("/dashboard/me")
    public ResponseEntity<MyDashboardResponse>
    getMyDashboard(
            Authentication authentication) {

        UUID customerId =
                userManagementService
                        .getUserEntityByEmail(
                                authentication.getName()
                        )
                        .getId();

        return ResponseEntity.ok(
                appointmentService
                        .getCustomerDashboard(
                                customerId
                        )
        );
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlots(
            @RequestParam UUID branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {

        return ResponseEntity.ok(availabilityService.getAvailableSlots(branchId, appointmentDate));
    }
}