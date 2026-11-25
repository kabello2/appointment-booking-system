package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.BranchRequest;
import com.javaguru.appointmentbooking.dto.response.BranchResponse;
import com.javaguru.appointmentbooking.dto.response.PagedResponse;
import com.javaguru.appointmentbooking.service.BranchService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Branches",
        description = """
                Branch management endpoints used to create,
                update, retrieve and deactivate branches.
                """
)
@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @Operation(
            summary = "Create Branch",
            description = """
                Creates a new branch that can be used
                for appointment booking and consultant assignment.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Branch created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions"
            )
    })
    @PostMapping
    @PreAuthorize("hasAuthority('BRANCH_CREATE')")
    public ResponseEntity<BranchResponse> createBranch(
            @Valid
            @RequestBody
            BranchRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchService.createBranch(request));
    }

    @Operation(
            summary = "Update Branch",
            description = """
                Updates the details of an existing branch.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Branch updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Branch not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            )
    })
    @PutMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_UPDATE')")
    public ResponseEntity<BranchResponse> updateBranch(

            @Parameter(
                    description = "Branch identifier",
                    required = true
            )
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchRequest request) {

        return ResponseEntity.ok(branchService.updateBranch(branchId, request));
    }

    @Operation(
            summary = "Get Branch",
            description = """
                Retrieves a branch by identifier.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Branch found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Branch not found"
            )
    })
    @GetMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<BranchResponse> getBranch(

            @Parameter(
                    description = "Branch identifier",
                    required = true
            )
            @PathVariable UUID branchId) {

        return ResponseEntity.ok(branchService.getBranch(branchId));
    }

    @Operation(
            summary = "Get Branches",
            description = """
                Retrieves all active branches available
                within the system.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Branches retrieved successfully"
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<PagedResponse<BranchResponse>> getBranches(Pageable pageable) {

        return ResponseEntity.ok(branchService.getBranches(pageable));
    }

    @Operation(
            summary = "Deactivate Branch",
            description = """
                Marks a branch as inactive.
                Inactive branches are excluded from
                future appointment bookings.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Branch deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Branch not found"
            )
    })
    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_DELETE')")
    public ResponseEntity<Void> deactivateBranch(
            @PathVariable UUID branchId) {

        branchService.deactivateBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
