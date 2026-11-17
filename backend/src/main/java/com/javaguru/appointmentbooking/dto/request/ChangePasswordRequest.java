package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(

        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
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
        String newPassword

) { }