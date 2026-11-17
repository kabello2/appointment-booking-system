package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateConsultantRequest(

        @NotBlank(message = "First name is required")
        @Size(
                min = 2,
                max = 50,
                message = "First name must be between 2 and 50 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z ]+$",
                message = "First name contains invalid characters"
        )
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(
                min = 2,
                max = 50,
                message = "Last name must be between 2 and 50 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z\\-\\' ]+$",
                message = "Last name contains invalid characters"
        )
        String lastName,

        @NotBlank(message = "Email address is required")
        @Email(message = "Invalid email address")
        @Size(
                max = 255,
                message = "Email address may not exceed 255 characters"
        )
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
                message = """
                        Password must contain at least one uppercase letter,
                        one lowercase letter, one number,
                        one special character and be between
                        12 and 64 characters long
                        """
        )
        String password,

        @NotNull(message = "Branch is required")
        UUID branchId

) { }