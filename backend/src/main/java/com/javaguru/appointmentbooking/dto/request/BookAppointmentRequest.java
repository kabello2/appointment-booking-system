package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record BookAppointmentRequest(
        @NotNull
        UUID branchId,

        @NotNull
        UUID serviceOfferingId,

        @NotNull
        LocalDate appointmentDate,

        @NotNull
        LocalTime startTime,

        String notes

) { }