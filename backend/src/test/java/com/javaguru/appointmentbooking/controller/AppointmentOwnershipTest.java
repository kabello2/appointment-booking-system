package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.BookAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.LoginRequest;
import com.javaguru.appointmentbooking.dto.request.RescheduleAppointmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentOwnershipTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String customerAToken;

    private String customerBToken;

    private UUID appointmentId;

    @BeforeEach
    void setup() throws Exception {

        customerAToken =
                registerAndLogin(
                        "customer.a@mail.com"
                );

        customerBToken =
                registerAndLogin(
                        "customer.b@mail.com"
                );

        UUID branchId =
                getFirstBranchId();

        UUID serviceId =
                getFirstServiceId();

        appointmentId =
                createAppointment(
                        customerAToken,
                        branchId,
                        serviceId
                );
    }

    @Test
    void customerBShouldNotViewCustomerAAppointment()
            throws Exception {

        mockMvc.perform(

                        get(
                                "/api/v1/appointments/{id}",
                                appointmentId
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + customerBToken
                                )

                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void customerBShouldNotCancelCustomerAAppointment()
            throws Exception {

        mockMvc.perform(

                        put(
                                "/api/v1/appointments/{id}/cancel",
                                appointmentId
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + customerBToken
                                )

                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void customerBShouldNotRescheduleCustomerAAppointment()
            throws Exception {

        RescheduleAppointmentRequest request =
                new RescheduleAppointmentRequest(
                        LocalDate.now().plusDays(5),
                        LocalTime.of(11, 0)
                );

        mockMvc.perform(

                        put(
                                "/api/v1/appointments/{id}/reschedule",
                                appointmentId
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + customerBToken
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )

                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void customerAShouldBeAbleToCancelOwnAppointment()
            throws Exception {

        mockMvc.perform(

                        put(
                                "/api/v1/appointments/{id}/cancel",
                                appointmentId
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + customerAToken
                                )

                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.status")
                                .value("CANCELLED")
                );
    }

    private String registerAndLogin(
            String email
    ) throws Exception {

        CreateUserRequest user =
                new CreateUserRequest(
                        "John",
                        "Doe",
                        email,
                        "0821234567",
                        "@Password2026",
                        null,
                        null
                );

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(
                                objectMapper.writeValueAsString(
                                        user
                                )
                        )
        );

        LoginRequest loginRequest =
                new LoginRequest(
                        email,
                        "@Password2026"
                );

        String response =
                mockMvc.perform(
                                post("/api/v1/auth/login")
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        loginRequest
                                                )
                                        )
                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return objectMapper
                .readTree(response)
                .get("accessToken")
                .asText();
    }

    private UUID getFirstBranchId()
            throws Exception {

        String response =
                mockMvc.perform(
                                get("/api/v1/branches")
                                        .header(
                                                "Authorization",
                                                "Bearer " + customerAToken
                                        )
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode branches =
                objectMapper.readTree(response);

        return UUID.fromString(
                branches
                        .get(0)
                        .get("id")
                        .asText()
        );
    }

    private UUID getFirstServiceId()
            throws Exception {

        String response =
                mockMvc.perform(
                                get("/api/v1/services")
                                        .header(
                                                "Authorization",
                                                "Bearer " + customerAToken
                                        )
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode services =
                objectMapper.readTree(response);

        return UUID.fromString(
                services
                        .get(0)
                        .get("id")
                        .asText()
        );
    }

    private UUID createAppointment(
            String token,
            UUID branchId,
            UUID serviceId
    ) throws Exception {

        BookAppointmentRequest request =
                new BookAppointmentRequest(
                        branchId,
                        serviceId,
                        LocalDate.now().plusDays(2),
                        LocalTime.of(9, 0),
                        null
                );

        String response =
                mockMvc.perform(
                                post(
                                        "/api/v1/appointments"
                                )
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        request
                                                )
                                        )
                        )
                        .andExpect(
                                status().isCreated()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        return UUID.fromString(
                objectMapper
                        .readTree(response)
                        .get("id")
                        .asText()
        );
    }
}
