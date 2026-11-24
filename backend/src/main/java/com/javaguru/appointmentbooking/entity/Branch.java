package com.javaguru.appointmentbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "branches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "branch_code",
                        columnNames = "branch_code"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch extends BaseAggregateRoot {

    @Column(nullable = false)
    private String branchCode;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(
            mappedBy = "branch",
            fetch = FetchType.LAZY
    )
    private Set<User> consultants =
            new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "branch",
            fetch = FetchType.LAZY
    )
    private Set<Appointment>
            appointments = new HashSet<>();

    private String addressLine1;

    private String addressLine2;

    private String suburb;

    private String city;

    private String province;

    private String postalCode;

    private String contactNumber;

    private Boolean active;

}
