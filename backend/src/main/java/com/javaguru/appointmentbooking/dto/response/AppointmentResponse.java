package com.javaguru.appointmentbooking.dto.response;

import com.javaguru.appointmentbooking.entity.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String appointmentNumber,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        UUID customerId,
        UUID consultantId,
        String consultantName,
        UUID branchId,
        String branchName,
        UUID serviceId,
        String serviceName

) { }