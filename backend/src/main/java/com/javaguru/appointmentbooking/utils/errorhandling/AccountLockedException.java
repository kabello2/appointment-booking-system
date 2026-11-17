package com.javaguru.appointmentbooking.utils.errorhandling;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }
}