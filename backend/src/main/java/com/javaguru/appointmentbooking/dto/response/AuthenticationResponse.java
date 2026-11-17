package com.javaguru.appointmentbooking.dto.response;

public record AuthenticationResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) { }
