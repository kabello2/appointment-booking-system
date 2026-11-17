package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleAppointmentRequest(

        @NotNull
        LocalDate appointmentDate,

        @NotNull
        LocalTime startTime

) { }