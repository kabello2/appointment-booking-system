package com.javaguru.appointmentbooking.factory;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.Role;

import java.util.UUID;

public final class UserTestDataFactory {

    private UserTestDataFactory() {
    }

    public static User customer() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("customer@test.com");
        user.setEnabled(true);
        user.setRole(Role.ROLE_CUSTOMER);

        return user;
    }

    public static User consultant() {

        User user = new User();

        user.setId(UUID.randomUUID());
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("consultant@test.com");
        user.setEnabled(true);
        user.setRole(Role.ROLE_CONSULTANT);

        return user;
    }
}