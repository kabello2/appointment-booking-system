package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignConsultantBranchRequest(
        @NotNull(message = "Branch is required")
        UUID branchId
) { }