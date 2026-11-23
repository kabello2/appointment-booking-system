package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.BookAppointmentRequest;
import com.javaguru.appointmentbooking.dto.response.AppointmentResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.Branch;
import com.javaguru.appointmentbooking.entity.ServiceOffering;
import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.factory.UserTestDataFactory;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.repository.ServiceOfferingRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ServiceOfferingRepository serviceOfferingRepository;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private ConsultantAllocationService consultantAllocationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldBookAppointmentSuccessfully() {

        User customer =
                UserTestDataFactory.customer();

        User consultant =
                UserTestDataFactory.consultant();

        Branch branch = new Branch();
        branch.setId(UUID.randomUUID());
        branch.setName("Sandton");
        branch.setActive(true);

        ServiceOffering serviceOffering =
                new ServiceOffering();

        serviceOffering.setId(UUID.randomUUID());
        serviceOffering.setName("Home Loan");
        serviceOffering.setDurationMinutes(30);
        serviceOffering.setActive(true);

        BookAppointmentRequest request =
                new BookAppointmentRequest(
                        branch.getId(),
                        serviceOffering.getId(),
                        LocalDate.now().plusDays(1),
                        LocalTime.of(10, 0),
                        "Test booking"
                );

        when(userRepository.findById(customer.getId()))
                .thenReturn(Optional.of(customer));

        when(branchRepository.findById(branch.getId()))
                .thenReturn(Optional.of(branch));

        when(serviceOfferingRepository.findById(serviceOffering.getId()))
                .thenReturn(Optional.of(serviceOffering));

        when(consultantAllocationService
                .allocateConsultant(
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(consultant);

        when(appointmentRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        AppointmentResponse response =
                appointmentService.bookAppointment(
                        customer.getId(),
                        request
                );

        assertNotNull(response);

        verify(notificationService)
                .createAppointmentBookedNotification(
                        any(Appointment.class)
                );
    }

    @Test
    void shouldThrowExceptionWhenBranchInactive() {

        User customer =
                UserTestDataFactory.customer();

        Branch branch = new Branch();
        branch.setActive(false);

        BookAppointmentRequest request =
                new BookAppointmentRequest(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDate.now().plusDays(1),
                        LocalTime.of(10, 0),
                        null
                );

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(customer));

        when(branchRepository.findById(any()))
                .thenReturn(Optional.of(branch));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> appointmentService.bookAppointment(
                        customer.getId(),
                        request
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenServiceInactive() {

        User customer =
                UserTestDataFactory.customer();

        Branch branch = new Branch();
        branch.setActive(true);

        ServiceOffering service =
                new ServiceOffering();

        service.setActive(false);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(customer));

        when(branchRepository.findById(any()))
                .thenReturn(Optional.of(branch));

        when(serviceOfferingRepository.findById(any()))
                .thenReturn(Optional.of(service));

        BookAppointmentRequest request =
                new BookAppointmentRequest(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDate.now().plusDays(1),
                        LocalTime.of(9, 0),
                        null
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> appointmentService.bookAppointment(
                        customer.getId(),
                        request
                )
        );
    }
}