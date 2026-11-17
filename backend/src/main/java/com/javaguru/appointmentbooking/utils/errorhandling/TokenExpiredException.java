package com.javaguru.appointmentbooking.utils.errorhandling;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}