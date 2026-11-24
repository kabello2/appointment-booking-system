package com.javaguru.appointmentbooking.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguru.appointmentbooking.dto.request.BookAppointmentRequest;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentFlowTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldRegisterLoginBookAndCancelAppointment()
            throws Exception {

        CreateUserRequest registerRequest =
                new CreateUserRequest(
                        "John",
                        "Doe",
                        "john.doe@mail.com",
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
                                                registerRequest
                                        )
                                )
                )
                .andExpect(
                        status().isCreated()
                );

        LoginRequest loginRequest =
                new LoginRequest(
                        "john.doe@mail.com",
                        "@Password2026"
                );

        String loginResponse =
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

        JsonNode loginJson =
                objectMapper.readTree(
                        loginResponse
                );

        String token =
                loginJson.get("accessToken")
                        .asText();

        assertThat(token)
                .isNotBlank();

        String currentUserResponse =
                mockMvc.perform(

                                get("/api/v1/users/current")
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )

                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode currentUser =
                objectMapper.readTree(
                        currentUserResponse
                );

        UUID customerId =
                UUID.fromString(
                        currentUser
                                .get("id")
                                .asText()
                );

        assertThat(customerId)
                .isNotNull();

        String branchesResponse =
                mockMvc.perform(

                                get("/api/v1/branches")
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )

                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode branchArray =
                objectMapper.readTree(
                        branchesResponse
                );

        UUID branchId =
                UUID.fromString(
                        branchArray
                                .get(0)
                                .get("id")
                                .asText()
                );

        String servicesResponse =
                mockMvc.perform(

                                get("/api/v1/services")
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )

                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode serviceArray =
                objectMapper.readTree(
                        servicesResponse
                );

        UUID serviceId =
                UUID.fromString(
                        serviceArray
                                .get(0)
                                .get("id")
                                .asText()
                );

        String slotsResponse =
                mockMvc.perform(

                                get("/api/v1/appointments/available-slots")
                                        .param(
                                                "branchId",
                                                branchId.toString()
                                        )
                                        .param(
                                                "appointmentDate",
                                                LocalDate.now()
                                                        .plusDays(2)
                                                        .toString()
                                        )
                                        .header(
                                                "Authorization",
                                                "Bearer " + token
                                        )

                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode availableSlots =
                objectMapper.readTree(
                        slotsResponse
                );


        BookAppointmentRequest request =
                new BookAppointmentRequest(
                        branchId,
                        serviceId,
                        LocalDate.now().plusDays(2),
                        LocalTime.now(),
                        null
                );

        String appointmentResponse =
                mockMvc.perform(

                                post("/api/v1/appointments")
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

        JsonNode appointment =
                objectMapper.readTree(
                        appointmentResponse
                );

        UUID appointmentId =
                UUID.fromString(
                        appointment
                                .get("id")
                                .asText()
                );

        assertThat(
                appointment.get("status")
                        .asText()
        ).isEqualTo("BOOKED");

        mockMvc.perform(

                        put(
                                "/api/v1/appointments/{appointmentId}/cancel",
                                appointmentId
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
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
}
