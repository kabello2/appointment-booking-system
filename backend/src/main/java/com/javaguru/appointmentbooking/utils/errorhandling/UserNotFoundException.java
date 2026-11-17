package com.javaguru.appointmentbooking.utils.errorhandling;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}