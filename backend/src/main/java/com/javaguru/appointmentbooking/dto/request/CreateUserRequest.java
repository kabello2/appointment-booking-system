package com.javaguru.appointmentbooking.dto.request;

import com.javaguru.appointmentbooking.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUserRequest(

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50)
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+27[0-9]{9}$",
                message = "Phone number must be in format +27821234567"
        )
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp =
                        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{12,64}$",
                message = "Password does not meet complexity requirements"
        )
        String password,

        Role role,

        UUID branchId
) { }
