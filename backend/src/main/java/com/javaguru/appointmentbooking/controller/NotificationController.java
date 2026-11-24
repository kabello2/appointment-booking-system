package com.javaguru.appointmentbooking.controller;

import com.javaguru.appointmentbooking.dto.response.NotificationResponse;
import com.javaguru.appointmentbooking.dto.response.NotificationSummaryResponse;
import com.javaguru.appointmentbooking.repository.NotificationRepository;
import com.javaguru.appointmentbooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<Page<NotificationResponse>> getNotifications(Pageable pageable) {

        return ResponseEntity.ok(notificationService.getNotifications(pageable));
    }

    @PostMapping("/{notificationId}/retry")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> retryNotification(@PathVariable UUID notificationId) {

        notificationService.retryNotification(notificationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<NotificationSummaryResponse> getSummary() {

        return ResponseEntity.ok(notificationService.getNotificationSummary());
    }

}