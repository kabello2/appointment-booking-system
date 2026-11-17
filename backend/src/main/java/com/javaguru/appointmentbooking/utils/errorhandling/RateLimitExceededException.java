package com.javaguru.appointmentbooking.utils.errorhandling;

public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}