package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.factory.UserTestDataFactory;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ConsultantAllocationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private ConsultantAllocationService consultantAllocationService;

    @Test
    void shouldSelectLeastLoadedConsultant() {

        User consultant1 =
                UserTestDataFactory.consultant();

        User consultant2 =
                UserTestDataFactory.consultant();

        UUID branchId = UUID.randomUUID();

        LocalDate appointmentDate =
                LocalDate.now();

        when(userRepository
                .findByRoleAndBranchIdAndEnabled(
                        Role.ROLE_CONSULTANT,
                        branchId,
                        true))
                .thenReturn(
                        List.of(
                                consultant1,
                                consultant2
                        )
                );

        when(availabilityService
                .isConsultantAvailable(
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(true);

        when(appointmentRepository
                .getConsultantActiveWorkload(
                        consultant1.getId(),
                        appointmentDate))
                .thenReturn(10L);

        when(appointmentRepository
                .getConsultantActiveWorkload(
                        consultant2.getId(),
                        appointmentDate))
                .thenReturn(2L);

        User selected =
                consultantAllocationService
                        .findAvailableConsultant(
                                branchId,
                                appointmentDate,
                                LocalTime.of(10, 0),
                                30
                        );

        assertEquals(
                consultant2.getId(),
                selected.getId()
        );
    }

    @Test
    void shouldThrowExceptionWhenNoConsultantsExist() {

        when(userRepository
                .findByRoleAndBranchIdAndEnabled(
                        any(),
                        any(),
                        eq(true)))
                .thenReturn(List.of());

        assertThrows(
                BusinessRuleViolationException.class,
                () -> consultantAllocationService
                        .findAvailableConsultant(
                                UUID.randomUUID(),
                                LocalDate.now(),
                                LocalTime.of(10, 0),
                                30
                        )
        );
    }
}