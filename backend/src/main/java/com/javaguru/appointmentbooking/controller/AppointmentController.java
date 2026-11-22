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
import io.swagger.v3.oas.annotations.Parameter;
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
        description = """
                Appointment booking, scheduling,
                cancellation, consultant workflow,
                customer dashboard and availability endpoints.
                """
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

    @Operation(
            summary = "Get My Appointments",
            description = """
                Retrieves all appointments belonging to the currently
                authenticated customer.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_VIEW_OWN', 'APPOINTMENT_VIEW')")
    public ResponseEntity<List<AppointmentResponse>>
    getMyAppointments(Authentication authentication) {

        return ResponseEntity.ok(appointmentService.getMyAppointments(authentication));
    }

    @Operation(
            summary = "Get Appointment Details",
            description = """
                Retrieves a single appointment by its identifier.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            )
    })
    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_VIEW','APPOINTMENT_VIEW_OWN')")
    public ResponseEntity<AppointmentResponse>
    getAppointment(
            @Parameter(
                    description = "Appointment identifier",
                    required = true
            )
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
    }

    @Operation(
            summary = "Cancel Appointment",
            description = """
Cancels an existing appointment.
Only appointments that have not been completed can be cancelled.
"""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Appointment cannot be cancelled"
            )
    })
    @PutMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_BOOK','APPOINTMENT_VIEW')")
    public ResponseEntity<AppointmentResponse>
    cancelAppointment(
            @Parameter(
                    description = "Appointment identifier",
                    required = true
            )
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }

    @Operation(
            summary = "Reschedule Appointment",
            description = """
                Changes the appointment date and time slot.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment rescheduled successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid reschedule request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            )
    })
    @PutMapping("/{appointmentId}/reschedule")
    @PreAuthorize("hasAnyAuthority('APPOINTMENT_BOOK','APPOINTMENT_VIEW')")
    public ResponseEntity<AppointmentResponse>
    rescheduleAppointment(
            @Parameter(
                    description = "Appointment identifier",
                    required = true
            )
            @PathVariable UUID appointmentId,

            @Valid
            @RequestBody
            RescheduleAppointmentRequest request) {

        return ResponseEntity.ok(appointmentService
                .rescheduleAppointment(appointmentId, request));
    }

    @Operation(
            summary = "Confirm Appointment",
            description = """
                Confirms a booked appointment.
                Typically performed by a consultant.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment confirmed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Appointment cannot be confirmed"
            )
    })
    @PutMapping("/{appointmentId}/confirm")
    @PreAuthorize("hasAuthority('APPOINTMENT_CONFIRM')")
    public ResponseEntity<AppointmentResponse>
    confirmAppointment(
            @Parameter(
                    description = "Appointment identifier",
                    required = true
            )
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.confirmAppointment(appointmentId));
    }

    @Operation(
            summary = "Complete Appointment",
            description = """
                Marks an appointment as completed.
                Typically performed by a consultant after servicing a customer.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment completed successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Appointment cannot be completed"
            )
    })

    @PutMapping("/{appointmentId}/complete")
    @PreAuthorize("hasAuthority('APPOINTMENT_COMPLETE')")
    public ResponseEntity<AppointmentResponse>
    completeAppointment(
            @Parameter(
                    description = "Appointment identifier",
                    required = true
            )
            @PathVariable UUID appointmentId) {

        return ResponseEntity.ok(appointmentService.completeAppointment(appointmentId));
    }

    @Operation(
            summary = "Get My Appointments By Status",
            description = """
                Retrieves customer appointments filtered by status.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments retrieved successfully"
            )
    })
    @GetMapping("/my/status")
    public ResponseEntity<List<AppointmentResponse>>
    getMyAppointmentsByStatus(

            Authentication authentication,

            @Parameter(
                    description = """
                        Appointment status
                        BOOKED,
                        CONFIRMED,
                        COMPLETED,
                        CANCELLED,
                        RESCHEDULED
                        """
            )
            @RequestParam AppointmentStatus status) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(customerId, status));
    }

    @Operation(
            summary = "Get My Appointments By Date Range",
            description = """
                Retrieves appointments falling within the supplied date range.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments retrieved successfully"
            )
    })
    @GetMapping("/my/date-range")
    public ResponseEntity<List<AppointmentResponse>>
    getMyAppointmentsByDateRange(

            Authentication authentication,

            @Parameter(
                    description = "Start date"
            )
            @RequestParam LocalDate fromDate,

            @Parameter(
                    description = "End date"
            )
            @RequestParam LocalDate toDate) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(customerId, fromDate, toDate));
    }

    @Operation(
            summary = "Get Upcoming Appointments",
            description = """
                Retrieves all future appointments for the authenticated customer.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Upcoming appointments retrieved successfully"
            )
    })
    @GetMapping("/my/upcoming")
    public ResponseEntity<List<AppointmentResponse>>
    getUpcomingAppointments(Authentication authentication) {

        UUID customerId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(customerId));
    }

    @Operation(
            summary = "Get Consultant Appointments",
            description = """
                Retrieves appointments assigned to the authenticated consultant.
                Used by the consultant work queue and schedule views.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consultant appointments retrieved successfully"
            )
    })
    @GetMapping("/consultant/my")
    @PreAuthorize("hasAuthority('APPOINTMENT_VIEW')")
    public ResponseEntity<List<AppointmentResponse>>
    getConsultantAppointments(Authentication authentication) {

        UUID consultantId = userManagementService.getUserEntityByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(appointmentService.getConsultantAppointments(consultantId));
    }

    @Operation(
            summary = "Get Customer Dashboard",
            description = """
                Retrieves dashboard statistics for the authenticated customer.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard retrieved successfully"
            )
    })
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

    @Operation(
            summary = "Get Available Slots",
            description = """
                Retrieves available appointment slots for a branch and date.
                Used during appointment booking and rescheduling.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Available slots retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })

    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotResponse>>
    getAvailableSlots(

            @Parameter(
                    description = "Branch identifier",
                    required = true
            )
            @RequestParam UUID branchId,

            @Parameter(
                    description = "Appointment date",
                    example = "2026-12-01",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate appointmentDate) {

        return ResponseEntity.ok(availabilityService.getAvailableSlots(branchId, appointmentDate));
    }
}