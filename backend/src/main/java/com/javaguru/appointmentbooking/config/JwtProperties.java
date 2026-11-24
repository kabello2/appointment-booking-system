package com.javaguru.appointmentbooking.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secretKey,
        Long accessTokenExpiration
) {

    @PostConstruct
    public void validate() {

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET_KEY is not configured."
            );
        }

        byte[] decoded = Base64.getDecoder().decode(secretKey);
        if (decoded.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET_KEY must be at least 256 bits."
            );

        }

    }
}
