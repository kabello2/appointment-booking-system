package com.javaguru.appointmentbooking.projection;

public interface MonthlyAppointmentProjection {
    Integer getYear();
    Integer getMonth();
    Long getAppointmentCount();
}