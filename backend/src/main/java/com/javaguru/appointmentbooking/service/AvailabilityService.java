package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.config.AppointmentProperties;
import com.javaguru.appointmentbooking.dto.response.AvailableSlotResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import com.javaguru.appointmentbooking.utils.errorhandling.DuplicateAppointmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentProperties appointmentProperties;

    public void validateAppointmentDate(
            LocalDate appointmentDate) {

        if (appointmentDate.isBefore(LocalDate.now())) {

            throw new BusinessRuleViolationException(
                    "Appointments cannot be booked in the past."
            );
        }
    }

    public void validateBusinessHours(
            LocalTime startTime,
            LocalTime endTime) {

        LocalTime businessStart =
                LocalTime.parse(
                        appointmentProperties
                                .businessHours()
                                .start()
                );

        LocalTime businessEnd =
                LocalTime.parse(
                        appointmentProperties
                                .businessHours()
                                .end()
                );

        if (startTime.isBefore(businessStart)
                || endTime.isAfter(businessEnd)) {

            throw new BusinessRuleViolationException(
                    "Appointment is outside business hours."
            );
        }
    }

    public void validateBranchAvailability(
            UUID branchId,
            LocalDate appointmentDate,
            LocalTime proposedStartTime,
            LocalTime proposedEndTime,
            UUID currentAppointmentId) {

        boolean overlaps =
                appointmentRepository
                        .findActiveAppointmentsForBranch(
                                branchId,
                                appointmentDate
                        )
                        .stream()

                        .filter(appointment ->
                                currentAppointmentId == null
                                        || !appointment.getId()
                                        .equals(currentAppointmentId)
                        )

                        .anyMatch(appointment ->

                                proposedStartTime.isBefore(
                                        appointment.getEndTime()
                                )

                                        &&

                                        proposedEndTime.isAfter(
                                                appointment.getStartTime()
                                        )
                        );

        if (overlaps) {

            throw new DuplicateAppointmentException(
                    "Selected appointment slot overlaps with an existing booking."
            );
        }
    }

    public boolean isConsultantAvailable(
            UUID consultantId,
            LocalDate appointmentDate,
            LocalTime proposedStartTime,
            LocalTime proposedEndTime) {

        return appointmentRepository
                .findActiveAppointmentsForConsultant(
                        consultantId,
                        appointmentDate
                )
                .stream()

                .noneMatch(appointment ->

                        proposedStartTime.isBefore(
                                appointment.getEndTime()
                        )

                                &&

                                proposedEndTime.isAfter(
                                        appointment.getStartTime()
                                )
                );
    }

    public List<AvailableSlotResponse> getAvailableSlots(
            UUID branchId,
            LocalDate appointmentDate) {

        List<Appointment> appointments =
                appointmentRepository
                        .findActiveAppointmentsForBranch(
                                branchId,
                                appointmentDate
                        );

        List<AvailableSlotResponse> slots =
                new ArrayList<>();

        LocalTime current =
                LocalTime.parse(
                        appointmentProperties
                                .businessHours()
                                .start()
                );

        LocalTime closing =
                LocalTime.parse(
                        appointmentProperties
                                .businessHours()
                                .end()
                );

        int slotDuration =
                appointmentProperties
                        .slotDurationMinutes();

        while (current.plusMinutes(slotDuration)
                .compareTo(closing) <= 0) {

            LocalTime slotEnd =
                    current.plusMinutes(slotDuration);

            LocalTime slotStart =
                    current;

            boolean occupied =
                    appointments.stream()
                            .anyMatch(appointment ->

                                    slotStart.isBefore(
                                            appointment.getEndTime()
                                    )

                                            &&

                                            slotEnd.isAfter(
                                                    appointment.getStartTime()
                                            )
                            );

            if (!occupied) {

                slots.add(
                        new AvailableSlotResponse(
                                slotStart.toString(),
                                slotEnd.toString()
                        )
                );
            }

            current =
                    current.plusMinutes(slotDuration);
        }

        return slots;
    }
}