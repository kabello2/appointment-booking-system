package com.javaguru.appointmentbooking.dto.response;


public record MonthlyAppointmentReportResponse(
        Integer year,
        Integer month,
        Long appointmentCount
) { }