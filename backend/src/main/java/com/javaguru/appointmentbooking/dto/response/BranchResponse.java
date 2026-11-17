package com.javaguru.appointmentbooking.dto.response;

import java.util.UUID;

public record BranchResponse(
        UUID id,
        String branchCode,
        String name,
        String addressLine1,
        String addressLine2,
        String suburb,
        String city,
        String province,
        String postalCode,
        String contactNumber,
        Boolean active

) { }