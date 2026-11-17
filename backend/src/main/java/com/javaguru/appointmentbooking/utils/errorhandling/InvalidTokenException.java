package com.javaguru.appointmentbooking.utils.errorhandling;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}