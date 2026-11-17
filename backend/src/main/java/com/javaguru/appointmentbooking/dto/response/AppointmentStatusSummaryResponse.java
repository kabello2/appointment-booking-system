package com.javaguru.appointmentbooking.dto.response;

public record AppointmentStatusSummaryResponse(
        long booked,
        long confirmed,
        long completed,
        long cancelled,
        long rescheduled
) { }