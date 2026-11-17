package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.request.AssignConsultantBranchRequest;
import com.javaguru.appointmentbooking.dto.response.ConsultantResponse;
import com.javaguru.appointmentbooking.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consultants")
@RequiredArgsConstructor
public class ConsultantController {

    private final UserManagementService
            userManagementService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<ConsultantResponse>>
    getConsultants() {

        return ResponseEntity.ok(
                userManagementService
                        .getConsultants()
        );
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<List<ConsultantResponse>>
    getConsultantsByBranch(
            @PathVariable UUID branchId) {

        return ResponseEntity.ok(
                userManagementService
                        .getConsultantsByBranch(
                                branchId
                        )
        );
    }

    @PutMapping("/{consultantId}/branch")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<ConsultantResponse>
    assignBranch(
            @PathVariable UUID consultantId,
            @Valid
            @RequestBody
            AssignConsultantBranchRequest request) {

        return ResponseEntity.ok(
                userManagementService
                        .assignBranch(
                                consultantId,
                                request
                        )
        );
    }
}