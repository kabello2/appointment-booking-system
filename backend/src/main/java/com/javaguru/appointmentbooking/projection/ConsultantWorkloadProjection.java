package com.javaguru.appointmentbooking.projection;

import java.util.UUID;

public interface ConsultantWorkloadProjection {
    UUID getConsultantId();
    String getFirstName();
    String getLastName();
    Long getAppointmentCount();
}