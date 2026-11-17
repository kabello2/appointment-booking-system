package com.javaguru.appointmentbooking.projection;

import java.util.UUID;

public interface BranchAppointmentReportProjection {

    UUID getBranchId();

    String getBranchCode();

    String getBranchName();

    Long getAppointmentCount();
}