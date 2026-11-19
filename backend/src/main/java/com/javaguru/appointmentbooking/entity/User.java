package com.javaguru.appointmentbooking.entity;

import com.javaguru.appointmentbooking.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseAggregateRoot {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "branch_id",
            foreignKey = @ForeignKey(name = "fk_user_branch")
    )
    private Branch branch;

    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY
    )
    private Set<Appointment>
            appointments = new HashSet<>();

    private boolean enabled;

    private boolean accountLocked;

    private boolean emailVerified;

    private Integer failedLoginAttempts = 0;

    private LocalDateTime lockedUntil;

}