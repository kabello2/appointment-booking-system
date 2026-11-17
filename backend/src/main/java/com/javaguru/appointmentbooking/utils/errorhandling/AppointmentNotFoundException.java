package com.javaguru.appointmentbooking.utils.errorhandling;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}