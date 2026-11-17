package com.javaguru.appointmentbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AppointmentControllerTest
        extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectUnauthenticatedBooking()
            throws Exception {

        String payload = """
                {
                    "branchId":"%s",
                    "serviceOfferingId":"%s",
                    "appointmentDate":"2026-12-01",
                    "startTime":"10:00"
                }
                """.formatted(
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/appointments")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(payload)
                )
                .andExpect(status().isUnauthorized());
    }
}