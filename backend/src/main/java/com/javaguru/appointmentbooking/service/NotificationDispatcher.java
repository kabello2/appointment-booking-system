package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.entity.Notification;
import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;
import com.javaguru.appointmentbooking.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatcher {

    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelayString = "${notification.dispatcher.fixed-delay-ms}")
    @Transactional
    public void dispatchNotifications() {

        List<Notification> notifications = notificationRepository.findTop100ByStatusOrderByCreatedAtAsc(NotificationStatus.PENDING);

        if (notifications.isEmpty()) {

            log.debug("No pending notifications found.");

            return;
        }

        log.info("Processing {} pending notification(s).", notifications.size());

        notifications.forEach(this::processNotification);
    }

    private void processNotification(Notification notification) {

        try {

            simulateNotificationSend(notification);

            notification.setStatus(NotificationStatus.SENT);

            notificationRepository.save(notification);

            log.info("Notification [{}] sent successfully.", notification.getId());

        } catch (Exception ex) {

            notification.setStatus(NotificationStatus.FAILED);

            notificationRepository.save(notification);

            log.error("Notification [{}] failed.", notification.getId(), ex);
        }
    }

    private void simulateNotificationSend(Notification notification) {

        log.info("""
                
                ====================================
                CHANNEL  : {}
                RECIPIENT: {}
                SUBJECT  : {}
                MESSAGE  : {}
                ====================================
                
                """, notification.getChannel(), notification.getRecipient(), notification.getSubject(), notification.getMessage());
    }
}