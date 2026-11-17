package com.javaguru.appointmentbooking.dto.response;

import java.time.LocalDate;

public record DailyAppointmentReportResponse(
        LocalDate appointmentDate,
        Long appointmentCount
) { }