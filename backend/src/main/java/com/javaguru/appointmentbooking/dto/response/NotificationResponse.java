package com.javaguru.appointmentbooking.dto.response;

import com.javaguru.appointmentbooking.entity.enums.NotificationChannel;
import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;

import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String recipient,
        String subject,
        String message,
        NotificationChannel channel,
        NotificationStatus status
) { }