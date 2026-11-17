package com.javaguru.appointmentbooking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "appointment")
public record AppointmentProperties(
        BusinessHours businessHours,
        Integer slotDurationMinutes
) {
    public record BusinessHours(
            String start,
            String end
    ) { }
}