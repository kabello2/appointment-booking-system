package com.javaguru.appointmentbooking.dto.response;

public record MyDashboardResponse(
        long upcomingAppointments,
        long completedAppointments,
        long cancelledAppointments
) {
}