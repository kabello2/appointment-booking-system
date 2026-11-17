package com.javaguru.appointmentbooking.dto.response;

import com.javaguru.appointmentbooking.entity.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Role role,
        Boolean enabled,
        UUID branchId
) { }
