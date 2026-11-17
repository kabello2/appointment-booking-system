package com.javaguru.appointmentbooking.dto.response;

public record NotificationSummaryResponse(
        long pending,
        long sent,
        long failed
) { }