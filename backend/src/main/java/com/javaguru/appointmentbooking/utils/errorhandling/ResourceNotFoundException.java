package com.javaguru.appointmentbooking.utils.errorhandling;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
