package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record ConsultantResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Boolean enabled,
        UUID branchId,
        String branchCode,
        String branchName
) { }