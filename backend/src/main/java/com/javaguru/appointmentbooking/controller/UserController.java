package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.UpdateUserRequest;
import com.javaguru.appointmentbooking.dto.response.PagedResponse;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "User Management",
        description = """
                User administration endpoints used to create,
                maintain, activate and deactivate system users.
                """
)
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;


    @Operation(
            summary = "Create User",
            description = """
                Creates a new system user such as
                an administrator, consultant or customer.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserResponse> createUser(
            @Valid
            @RequestBody
            CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createUser(request));
    }

    @Operation(
            summary = "Get Users",
            description = """
                Retrieves all active users in the system.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully"
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<PagedResponse<UserResponse>> getUsers(Pageable pageable) {

        return ResponseEntity.ok(userManagementService.getUsers(pageable));
    }

    @Operation(
            summary = "Get User",
            description = """
                Retrieves a user by identifier.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<UserResponse> getUser(

            @Parameter(
                    description = "User identifier",
                    required = true
            )
            @PathVariable UUID userId) {

        return ResponseEntity.ok(userManagementService.getUser(userId));
    }

    @Operation(
            summary = "Update User",
            description = """
                Updates user details and role information.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )
    })
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserResponse> updateUser(

            @Parameter(
                    description = "User identifier",
                    required = true
            )
            @PathVariable UUID userId,
            @Valid
            @RequestBody
            UpdateUserRequest request) {

        return ResponseEntity.ok(userManagementService.updateUser(userId, request));
    }


    @Operation(
            summary = "Delete User",
            description = """
                Deactivates a user account.
                The user remains in the system
                for audit purposes.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteUser(

            @Parameter(
                    description = "User identifier",
                    required = true
            )
            @PathVariable UUID userId) {

        userManagementService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get Current User",
            description = """
                Retrieves profile information
                for the currently authenticated user.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current user retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            )
    })
    @GetMapping("/current")
    public ResponseEntity<UserResponse> current(
            Authentication authentication) {

        return ResponseEntity.ok(userManagementService.getUserByEmail(authentication.getName()));
    }

    @Operation(
            summary = "Disable User",
            description = """
                Disables a user account and prevents
                future authentication.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User disabled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{userId}/disable")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> disableUser(

            @Parameter(
                    description = "User identifier",
                    required = true
            )
            @PathVariable UUID userId) {

        userManagementService.disableUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Enable User",
            description = """
                Re-enables a previously disabled account.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "User enabled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{userId}/enable")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void>enableUser(

            @Parameter(
                    description = "User identifier",
                    required = true
            )
            @PathVariable UUID userId) {

        userManagementService.enableUser(userId);
        return ResponseEntity.noContent().build();
    }
}