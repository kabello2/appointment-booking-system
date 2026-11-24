package com.javaguru.appointmentbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest
        extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterUserSuccessfully()
            throws Exception {

        String payload = """
                {
                    "firstName":"John",
                    "lastName":"Doe",
                    "email":"john@test.com",
                    "phoneNumber":"+27821234567",
                    "password":"Password@123"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(payload)
                )
                .andExpect(status().isCreated());
    }
}