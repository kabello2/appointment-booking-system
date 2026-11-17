package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.BookAppointmentRequest;
import com.javaguru.appointmentbooking.dto.request.RescheduleAppointmentRequest;
import com.javaguru.appointmentbooking.dto.response.AppointmentResponse;
import com.javaguru.appointmentbooking.dto.response.MyDashboardResponse;
import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.Branch;
import com.javaguru.appointmentbooking.entity.ServiceOffering;
import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.AppointmentStatus;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.repository.ServiceOfferingRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final AvailabilityService availabilityService;
    private final ConsultantAllocationService consultantAllocationService;
    private final NotificationService notificationService;

    public AppointmentResponse bookAppointment(
            UUID customerId,
            BookAppointmentRequest request) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Customer not found."));

        Branch branch = branchRepository.findById(
                        request.branchId())
                .orElseThrow(() ->
                        new BranchNotFoundException(
                                "Branch not found."
                        )
                );

        if (!Boolean.TRUE.equals(branch.getActive())) {

            throw new BusinessRuleViolationException(
                    "Selected branch is inactive."
            );
        }

        ServiceOffering serviceOffering =
                serviceOfferingRepository.findById(
                                request.serviceOfferingId())
                        .orElseThrow(
                                () ->
                                        new ServiceOfferingNotFoundException(
                                                "Service offering not found."
                                        )
                        );

        if (!Boolean.TRUE.equals(
                serviceOffering.getActive())) {

            throw new BusinessRuleViolationException(
                    "Selected service offering is inactive."
            );
        }

        LocalTime proposedEndTime =
                request.startTime()
                        .plusMinutes(
                                serviceOffering.getDurationMinutes()
                        );

        availabilityService.validateAppointmentDate(
                request.appointmentDate()
        );

        availabilityService.validateBusinessHours(
                request.startTime(),
                proposedEndTime
        );

        availabilityService.validateBranchAvailability(
                branch.getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                null
        );

        User consultant =
                consultantAllocationService
                        .findAvailableConsultant(
                                branch.getId(),
                                request.appointmentDate(),
                                request.startTime(),
                                serviceOffering.getDurationMinutes()
                        );

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentNumber(
                generateAppointmentNumber()
        );

        appointment.setCustomer(customer);

        appointment.setConsultant(
                consultant
        );

        appointment.setBranch(branch);

        appointment.setServiceOffering(
                serviceOffering
        );

        appointment.setAppointmentDate(
                request.appointmentDate()
        );

        appointment.setStartTime(
                request.startTime()
        );

        appointment.setEndTime(
                proposedEndTime
        );

        appointment.setStatus(
                AppointmentStatus.BOOKED
        );

        appointment.setNotes(
                request.notes()
        );

        Appointment saved =
                appointmentRepository.save(
                        appointment
                );

        notificationService
                .createAppointmentBookedNotification(
                        saved
                );

        return mapToResponse(saved);
    }

    public AppointmentResponse getAppointment(
            UUID appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(
                                                "Appointment not found."
                                        )
                        );

        return mapToResponse(appointment);
    }

    public List<AppointmentResponse> getMyAppointments(
            UUID customerId) {

        return appointmentRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AppointmentResponse cancelAppointment(
            UUID appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(
                                                "Appointment not found."
                                        )
                        );

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new BusinessRuleViolationException(
                    "Completed appointments cannot be cancelled."
            );
        }

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        Appointment saved =
                appointmentRepository.save(
                        appointment
                );

        return mapToResponse(saved);
    }

    public AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            RescheduleAppointmentRequest request) {

        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(
                                                "Appointment not found."
                                        )
                        );

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new BusinessRuleViolationException(
                    "Completed appointments cannot be rescheduled."
            );
        }

        LocalTime proposedEndTime =
                request.startTime()
                        .plusMinutes(
                                appointment
                                        .getServiceOffering()
                                        .getDurationMinutes()
                        );

        availabilityService.validateAppointmentDate(
                request.appointmentDate()
        );

        availabilityService.validateBusinessHours(
                request.startTime(),
                proposedEndTime
        );

        availabilityService.validateBranchAvailability(
                appointment.getBranch().getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                appointment.getId()
        );

        appointment.setAppointmentDate(
                request.appointmentDate()
        );

        appointment.setStartTime(
                request.startTime()
        );

        appointment.setEndTime(
                proposedEndTime
        );

        appointment.setStatus(
                AppointmentStatus.RESCHEDULED
        );

        Appointment saved =
                appointmentRepository.save(
                        appointment
                );

        return mapToResponse(saved);
    }

    public AppointmentResponse confirmAppointment(
            UUID appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(
                                                "Appointment not found."
                                        )
                        );

        appointment.setStatus(
                AppointmentStatus.CONFIRMED
        );

        return mapToResponse(
                appointmentRepository.save(
                        appointment
                )
        );
    }

    public AppointmentResponse completeAppointment(
            UUID appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(
                                appointmentId)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(
                                                "Appointment not found."
                                        )
                        );

        if (appointment.getStatus()
                != AppointmentStatus.CONFIRMED) {

            throw new BusinessRuleViolationException(
                    "Only confirmed appointments can be completed."
            );
        }

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        return mapToResponse(
                appointmentRepository.save(
                        appointment
                )
        );
    }

    private String generateAppointmentNumber() {

        return "APT-"
                + LocalDate.now()
                .format(
                        DateTimeFormatter.BASIC_ISO_DATE
                )
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public List<AppointmentResponse>
    getAppointmentsByStatus(
            UUID customerId,
            AppointmentStatus status) {

        return appointmentRepository
                .findByCustomerIdAndStatus(
                        customerId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AppointmentResponse>
    getAppointmentsByDateRange(
            UUID customerId,
            LocalDate fromDate,
            LocalDate toDate) {

        return appointmentRepository
                .findByCustomerIdAndAppointmentDateBetween(
                        customerId,
                        fromDate,
                        toDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AppointmentResponse> getUpcomingAppointments(UUID customerId) {

        return appointmentRepository
                .findByCustomerIdAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
                        customerId,
                        LocalDate.now()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AppointmentResponse> getConsultantAppointments(UUID consultantId) {

        return appointmentRepository
                .findByConsultantIdOrderByAppointmentDateAscStartTimeAsc(
                        consultantId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MyDashboardResponse getCustomerDashboard(UUID customerId) {

        return new MyDashboardResponse(

                appointmentRepository
                        .countUpcomingAppointments(
                                customerId,
                                LocalDate.now()
                        ),

                appointmentRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                AppointmentStatus.COMPLETED
                        ),

                appointmentRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                AppointmentStatus.CANCELLED
                        )
        );
    }

    private AppointmentResponse mapToResponse(
            Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentNumber(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus(),

                appointment.getCustomer().getId(),

                appointment.getConsultant().getId(),

                appointment.getConsultant()
                        .getFirstName()
                        + " "
                        + appointment.getConsultant()
                        .getLastName(),

                appointment.getBranch().getId(),
                appointment.getBranch().getName(),

                appointment.getServiceOffering().getId(),
                appointment.getServiceOffering().getName()
        );
    }
}