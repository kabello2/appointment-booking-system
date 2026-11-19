package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.UpdateUserRequest;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;


    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> createUser(
            @Valid
            @RequestBody
            CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<UserResponse>> getUsers() {

        return ResponseEntity.ok(userManagementService.getUsers());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(userManagementService.getUser(userId));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID userId,
            @Valid
            @RequestBody
            UpdateUserRequest request) {

        return ResponseEntity.ok(userManagementService.updateUser(userId, request));
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> disableUser(
            @PathVariable UUID userId) {

        userManagementService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current")
    public ResponseEntity<UserResponse> current(
            Authentication authentication) {

        return ResponseEntity.ok(userManagementService.getUserByEmail(authentication.getName()));
    }
}