package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.ChangePasswordRequest;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.LoginRequest;
import com.javaguru.appointmentbooking.dto.response.AuthenticationResponse;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication",
        description = """
                Authentication and account management endpoints
                including registration, login and password management.
                """
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register Customer",
            description = """
                Registers a new customer account and returns
                the created user details.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid
            @RequestBody
            CreateUserRequest request) {

        UserResponse response = authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Authenticate User",
            description = """
                Authenticates a user using email and password
                and returns a JWT access token with user details.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid
            @RequestBody
            LoginRequest request) {

        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Change Password",
            description = """
                Changes the password of the currently
                authenticated user.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Password changed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid password request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal
            UserDetails principal,

            @Valid
            @RequestBody
            ChangePasswordRequest request) {

        authenticationService.changePassword(
                principal.getUsername(),
                request
        );

        return ResponseEntity.noContent().build();
    }
}