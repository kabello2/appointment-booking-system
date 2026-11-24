package com.javaguru.appointmentbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTest
        extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRequireAuthenticationForNotifications()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/notifications")
                )
                .andExpect(status().isUnauthorized());
    }
}
