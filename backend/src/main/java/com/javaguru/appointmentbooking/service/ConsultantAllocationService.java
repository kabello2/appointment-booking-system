package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultantAllocationService {

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    private final AvailabilityService availabilityService;

    public User allocateConsultant(
            UUID branchId,
            LocalDate appointmentDate,
            LocalTime startTime,
            Integer durationMinutes) {

        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        List<User> consultants = userRepository
                .findByRoleAndBranchIdAndEnabled(
                        Role.ROLE_CONSULTANT,
                        branchId,
                        true
                );

        if (consultants.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "No consultants found for selected branch."
            );
        }

        List<User> availableConsultants = consultants
                .stream()
                .filter(consultant ->
                                availabilityService.isConsultantAvailable(
                                        consultant.getId(),
                                        appointmentDate,
                                        startTime,
                                        endTime)).toList();

        if (availableConsultants.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "No consultant available for selected appointment slot.");
        }

        return availableConsultants.stream()
                .min(Comparator.comparingLong(consultant ->
                        appointmentRepository.getConsultantActiveWorkload(
                                consultant.getId(),
                                appointmentDate
                        )
                ))
                .orElseThrow(() ->
                        new BusinessRuleViolationException("Unable to allocate consultant."));
    }
}