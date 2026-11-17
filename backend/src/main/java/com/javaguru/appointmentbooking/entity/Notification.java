package com.javaguru.appointmentbooking.entity;

import com.javaguru.appointmentbooking.entity.enums.NotificationChannel;
import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends BaseAggregateRoot {

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(
            nullable = false,
            length = 3000
    )
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id"
    )
    private Appointment appointment;
}
