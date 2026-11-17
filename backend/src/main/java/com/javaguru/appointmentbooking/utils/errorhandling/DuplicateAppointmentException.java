package com.javaguru.appointmentbooking.utils.errorhandling;

public class DuplicateAppointmentException extends RuntimeException {

    public DuplicateAppointmentException(String message) {
        super(message);
    }
}
