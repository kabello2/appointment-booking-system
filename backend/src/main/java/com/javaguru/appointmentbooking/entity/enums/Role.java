package com.javaguru.appointmentbooking.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN(
            Set.of(
                    Permission.USER_CREATE,
                    Permission.USER_VIEW,
                    Permission.USER_UPDATE,
                    Permission.USER_DELETE,
                    Permission.BRANCH_CREATE,
                    Permission.BRANCH_VIEW,
                    Permission.BRANCH_UPDATE,
                    Permission.BRANCH_DELETE,
                    Permission.APPOINTMENT_VIEW,
                    Permission.APPOINTMENT_CONFIRM,
                    Permission.APPOINTMENT_COMPLETE
            )
    ),

    ROLE_CONSULTANT(
            Set.of(
                    Permission.APPOINTMENT_VIEW,
                    Permission.APPOINTMENT_CONFIRM,
                    Permission.APPOINTMENT_COMPLETE
            )
    ),

    ROLE_CUSTOMER(
            Set.of(
                    Permission.APPOINTMENT_BOOK,
                    Permission.APPOINTMENT_VIEW_OWN
            )
    );

    private final Set<Permission> permissions;
}