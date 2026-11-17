package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.response.NotificationResponse;
import com.javaguru.appointmentbooking.dto.response.NotificationSummaryResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.Notification;
import com.javaguru.appointmentbooking.entity.enums.NotificationChannel;
import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;
import com.javaguru.appointmentbooking.repository.NotificationRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.NotificationNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createAppointmentBookedNotification(Appointment appointment) {

        Notification notification = new Notification();

        notification.setRecipient(appointment.getCustomer().getEmail());

        notification.setSubject("Appointment Booking Confirmation");

        notification.setMessage(String.format("""
                Your appointment %s has been booked successfully.
                
                Branch: %s
                
                Service: %s
                
                Date: %s
                
                Time: %s
                """,
                appointment.getAppointmentNumber(),
                appointment.getBranch().getName(),
                appointment.getServiceOffering().getName(),
                appointment.getAppointmentDate(),
                appointment.getStartTime()));

        notification.setChannel(NotificationChannel.EMAIL);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setAppointment(appointment);

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Transactional
    public void retryNotification(UUID notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found."));

        notification.setStatus(NotificationStatus.PENDING);

        notificationRepository.save(notification);
    }

    @Transactional
    public NotificationSummaryResponse getNotificationSummary() {

        return new NotificationSummaryResponse(
                notificationRepository.countByStatus(NotificationStatus.PENDING),
                notificationRepository.countByStatus(NotificationStatus.SENT),
                notificationRepository.countByStatus(NotificationStatus.FAILED));
    }

    private NotificationResponse mapToResponse(Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getChannel(),
                notification.getStatus()
        );
    }
}