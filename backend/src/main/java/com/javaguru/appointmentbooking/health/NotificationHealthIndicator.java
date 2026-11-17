package com.javaguru.appointmentbooking.health;

import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;
import com.javaguru.appointmentbooking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationHealthIndicator implements HealthIndicator {

    private final NotificationRepository
            notificationRepository;

    @Override
    public Health health() {

        long pending =
                notificationRepository.countByStatus(
                        NotificationStatus.PENDING
                );

        long failed =
                notificationRepository.countByStatus(
                        NotificationStatus.FAILED
                );

        return Health.up()
                .withDetail(
                        "pendingNotifications",
                        pending
                )
                .withDetail(
                        "failedNotifications",
                        failed
                )
                .build();
    }
}