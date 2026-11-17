package com.javaguru.appointmentbooking.utils.errorhandling;

public class AccountDisabledException extends RuntimeException {

    public AccountDisabledException(String message) {
        super(message);
    }
}
