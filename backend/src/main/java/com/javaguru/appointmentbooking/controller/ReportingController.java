package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.response.*;
import com.javaguru.appointmentbooking.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {

        return ResponseEntity.ok(reportingService.getDashboardSummary());
    }

    @GetMapping("/appointments/status")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<AppointmentStatusSummaryResponse> getAppointmentStatusSummary() {

        return ResponseEntity.ok(reportingService.getAppointmentStatusSummary());
    }

    @GetMapping("/appointments/branches")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<BranchAppointmentReportResponse>> getAppointmentsPerBranch() {

        return ResponseEntity.ok(reportingService.getAppointmentCountPerBranch());
    }

    @GetMapping("/services/popularity")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<ServicePopularityResponse>> getServicePopularity() {

        return ResponseEntity.ok(reportingService.getServicePopularity());
    }

    @GetMapping("/appointments/daily")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<DailyAppointmentReportResponse>> getAppointmentsPerDay() {

        return ResponseEntity.ok(reportingService.getAppointmentCountPerDay());
    }
}