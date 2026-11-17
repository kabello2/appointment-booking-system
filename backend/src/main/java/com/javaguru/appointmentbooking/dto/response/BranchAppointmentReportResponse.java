package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record BranchAppointmentReportResponse(
        UUID branchId,
        String branchCode,
        String branchName,
        Long appointmentCount
) { }