package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.config.AppointmentProperties;
import com.javaguru.appointmentbooking.dto.response.AvailableSlotResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.enums.Role;
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

import com.javaguru.appointmentbooking.config.AppointmentProperties;
import com.javaguru.appointmentbooking.dto.response.AvailableSlotResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
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
    private final UserRepository userRepository;
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

    public void validateConsultantAvailability(
            UUID consultantId,
            LocalDate appointmentDate,
            LocalTime proposedStartTime,
            LocalTime proposedEndTime,
            UUID currentAppointmentId) {

        boolean overlaps =
                appointmentRepository
                        .findActiveAppointmentsForConsultant(
                                consultantId,
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
                    "Consultant already has an appointment during this time."
            );
        }
    }

    public void validateCustomerAvailability(
            UUID customerId,
            LocalDate appointmentDate,
            LocalTime proposedStartTime,
            LocalTime proposedEndTime,
            UUID currentAppointmentId) {

        boolean overlaps =
                appointmentRepository
                        .findActiveAppointmentsForCustomer(
                                customerId,
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
                    "Customer already has an appointment during this time."
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

        List<User> consultants = userRepository.findByRoleAndBranchIdAndEnabled(
                Role.ROLE_CONSULTANT,
                branchId,
                true
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

            LocalTime slotStart = current;

            LocalTime slotEnd =
                    current.plusMinutes(slotDuration);

            boolean available =
                    consultants.stream()
                            .anyMatch(consultant ->
                                    isConsultantAvailable(
                                            consultant.getId(),
                                            appointmentDate,
                                            slotStart,
                                            slotEnd
                                    )
                            );

            if (available) {

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