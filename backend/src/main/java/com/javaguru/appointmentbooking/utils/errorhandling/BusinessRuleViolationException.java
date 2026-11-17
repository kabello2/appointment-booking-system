package com.javaguru.appointmentbooking.utils.errorhandling;

public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}