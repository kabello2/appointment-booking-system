package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record ConsultantWorkloadResponse(
        UUID consultantId,
        String firstName,
        String lastName,
        Long appointmentCount
) { }