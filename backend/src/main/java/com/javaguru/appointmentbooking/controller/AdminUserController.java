package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.CreateConsultantRequest;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserManagementService userManagementService;

    @PostMapping("/admins")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> createAdmin(
            @Valid
            @RequestBody
            CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createAdmin(request));
    }

    @PostMapping("/consultants")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> createConsultant(
            @Valid
            @RequestBody
            CreateConsultantRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createConsultant(request));
    }

}