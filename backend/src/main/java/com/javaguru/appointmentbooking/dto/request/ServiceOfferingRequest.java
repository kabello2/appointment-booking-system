package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceOfferingRequest(
        @NotBlank(message = "Service code is required")
        String serviceCode,

        @NotBlank(message = "Service name is required")
        String name,

        String description,

        @NotNull(message = "Duration is required")
        @Min(value = 15)
        Integer durationMinutes

) { }