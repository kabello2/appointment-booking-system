package com.javaguru.appointmentbooking.utils.errorhandling;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}