package com.javaguru.appointmentbooking.dto.response;

public record AvailableSlotResponse(
        String startTime,
        String endTime
) { }