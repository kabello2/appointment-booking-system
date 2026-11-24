package com.javaguru.appointmentbooking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "security.rate-limit"
)
public record RateLimitProperties(
        Login login,
        Registration registration

) {

    public record Login(
            long capacity,
            long durationMinutes
    ) {
    }

    public record Registration(
            long capacity,
            long durationHours
    ) {
    }

}