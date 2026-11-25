package com.javaguru.appointmentbooking.dto.response;

import java.util.List;

public record PagedResponse<T>(

        List<T> items,

        long totalElements,

        int totalPages,

        int currentPage,

        int pageSize

) {
}
