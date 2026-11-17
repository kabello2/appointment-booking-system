package com.javaguru.appointmentbooking.projection;

import java.util.UUID;

public interface ServicePopularityProjection {
    UUID getServiceId();
    String getServiceCode();
    String getServiceName();
    Long getBookingCount();
}