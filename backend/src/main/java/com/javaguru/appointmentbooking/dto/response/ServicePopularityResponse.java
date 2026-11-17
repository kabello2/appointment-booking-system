package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record ServicePopularityResponse(
        UUID serviceId,
        String serviceCode,
        String serviceName,
        Long bookingCount
) { }