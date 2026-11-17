package com.javaguru.appointmentbooking.utils.errorhandling;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
