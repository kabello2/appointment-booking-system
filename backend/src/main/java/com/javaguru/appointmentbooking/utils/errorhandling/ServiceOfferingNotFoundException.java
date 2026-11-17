package com.javaguru.appointmentbooking.utils.errorhandling;

public class ServiceOfferingNotFoundException extends RuntimeException {

    public ServiceOfferingNotFoundException(String message) {
        super(message);
    }
}