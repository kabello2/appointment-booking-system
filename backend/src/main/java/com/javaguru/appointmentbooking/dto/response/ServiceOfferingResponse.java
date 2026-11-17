package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record ServiceOfferingResponse(
        UUID id,
        String serviceCode,
        String name,
        String description,
        Integer durationMinutes,
        Boolean active
) { }