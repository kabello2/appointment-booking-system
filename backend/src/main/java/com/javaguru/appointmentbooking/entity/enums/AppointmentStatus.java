package com.javaguru.appointmentbooking.entity.enums;

public enum AppointmentStatus {

    BOOKED,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    RESCHEDULED;

    public boolean canTransitionTo(
            AppointmentStatus target
    ) {

        return switch (this) {

            case BOOKED ->
                    target == CONFIRMED
                            || target == CANCELLED
                            || target == RESCHEDULED;

            case CONFIRMED ->
                    target == COMPLETED
                            || target == CANCELLED
                            || target == RESCHEDULED;

            case RESCHEDULED ->
                    target == CONFIRMED
                            || target == CANCELLED;

            case COMPLETED -> false;

            case CANCELLED -> false;
        };

    }
}
