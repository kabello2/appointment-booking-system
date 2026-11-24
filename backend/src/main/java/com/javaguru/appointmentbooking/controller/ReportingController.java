package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.response.*;
import com.javaguru.appointmentbooking.service.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Reporting",
        description = """
                Reporting and dashboard analytics endpoints.
                Provides appointment statistics, branch reports,
                service popularity metrics and dashboard summaries.
                """
)
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @Operation(
            summary = "Get Dashboard Summary",
            description = """
                Retrieves overall dashboard metrics including
                appointment counts and summary statistics.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard summary retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {

        return ResponseEntity.ok(reportingService.getDashboardSummary());
    }

    @Operation(
            summary = "Get Appointment Status Summary",
            description = """
                Retrieves appointment counts grouped by status
                including booked, confirmed, completed,
                cancelled and rescheduled appointments.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment status summary retrieved successfully"
            )
    })
    @GetMapping("/appointments/status")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<AppointmentStatusSummaryResponse> getAppointmentStatusSummary() {

        return ResponseEntity.ok(reportingService.getAppointmentStatusSummary());
    }

    @Operation(
            summary = "Get Appointments Per Branch",
            description = """
                Retrieves appointment statistics grouped by branch.
                Useful for branch performance analysis.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Branch appointment report retrieved successfully"
            )
    })
    @GetMapping("/appointments/branches")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<BranchAppointmentReportResponse>> getAppointmentsPerBranch() {

        return ResponseEntity.ok(reportingService.getAppointmentCountPerBranch());
    }

    @Operation(
            summary = "Get Service Popularity Report",
            description = """
                Retrieves appointment counts grouped by
                service offering, ranked by popularity.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Service popularity report retrieved successfully"
            )
    })
    @GetMapping("/services/popularity")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<ServicePopularityResponse>> getServicePopularity() {

        return ResponseEntity.ok(reportingService.getServicePopularity());
    }

    @Operation(
            summary = "Get Daily Appointment Report",
            description = """
                Retrieves appointment volumes grouped by day.
                Used for trend analysis and dashboard charts.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Daily appointment report retrieved successfully"
            )
    })
    @GetMapping("/appointments/daily")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<DailyAppointmentReportResponse>> getAppointmentsPerDay() {

        return ResponseEntity.ok(reportingService.getAppointmentCountPerDay());
    }
}