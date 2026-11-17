package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.config.AppointmentProperties;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentProperties appointmentProperties;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void shouldRejectPastAppointmentDate() {

        assertThrows(
                BusinessRuleViolationException.class,
                () -> availabilityService
                        .validateAppointmentDate(
                                LocalDate.now().minusDays(1)
                        )
        );
    }

    @Test
    void shouldAllowFutureAppointmentDate() {

        assertDoesNotThrow(
                () -> availabilityService
                        .validateAppointmentDate(
                                LocalDate.now().plusDays(1)
                        )
        );
    }
}