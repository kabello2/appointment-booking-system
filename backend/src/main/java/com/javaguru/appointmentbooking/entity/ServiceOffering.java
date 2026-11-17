package com.javaguru.appointmentbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "service_offerings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "service_code",
                        columnNames = "service_code"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ServiceOffering extends BaseAggregateRoot {

    @Column(nullable = false)
    private String serviceCode;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(
            mappedBy = "serviceOffering",
            fetch = FetchType.LAZY
    )
    private Set<Appointment>
            appointments = new HashSet<>();
}
