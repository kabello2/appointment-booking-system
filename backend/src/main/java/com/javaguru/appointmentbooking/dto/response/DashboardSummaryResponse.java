package com.javaguru.appointmentbooking.dto.response;

public record DashboardSummaryResponse(
        long totalCustomers,
        long totalConsultants,
        long totalBranches,
        long totalServices,
        long totalAppointments,
        long appointmentsToday,
        long completedAppointments,
        long pendingNotifications
) { }