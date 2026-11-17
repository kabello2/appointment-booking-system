package com.javaguru.appointmentbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequest(

        @NotBlank(message = "Branch code is required")
        @Size(max = 50)
        String branchCode,

        @NotBlank(message = "Branch name is required")
        @Size(max = 255)
        String name,
        String addressLine1,
        String addressLine2,
        String suburb,
        String city,
        String province,
        String postalCode,
        String contactNumber

) { }