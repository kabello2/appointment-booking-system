package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.BranchRequest;
import com.javaguru.appointmentbooking.dto.response.BranchResponse;
import com.javaguru.appointmentbooking.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasAuthority('BRANCH_CREATE')")
    public ResponseEntity<BranchResponse> createBranch(
            @Valid
            @RequestBody
            BranchRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchService.createBranch(request));
    }

    @PutMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_UPDATE')")
    public ResponseEntity<BranchResponse> updateBranch(
            @PathVariable UUID branchId,
            @Valid @RequestBody BranchRequest request) {

        return ResponseEntity.ok(branchService.updateBranch(branchId, request));
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<BranchResponse> getBranch(
            @PathVariable UUID branchId) {

        return ResponseEntity.ok(branchService.getBranch(branchId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BRANCH_VIEW')")
    public ResponseEntity<List<BranchResponse>>
    getBranches() {

        return ResponseEntity.ok(branchService.getBranches());
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasAuthority('BRANCH_DELETE')")
    public ResponseEntity<Void> deactivateBranch(
            @PathVariable UUID branchId) {

        branchService.deactivateBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
