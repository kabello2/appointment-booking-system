package com.javaguru.appointmentbooking.utils.errorhandling;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}