package com.javaguru.appointmentbooking.projection;

import java.time.LocalDate;

public interface DailyAppointmentProjection {
    LocalDate getAppointmentDate();
    Long getAppointmentCount();
}