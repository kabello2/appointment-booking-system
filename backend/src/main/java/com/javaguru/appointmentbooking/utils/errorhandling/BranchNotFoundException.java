package com.javaguru.appointmentbooking.utils.errorhandling;

public class BranchNotFoundException extends RuntimeException {

    public BranchNotFoundException(String message) {
        super(message);
    }
}