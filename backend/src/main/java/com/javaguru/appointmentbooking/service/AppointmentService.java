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
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.repository.AppointmentRepository;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.repository.ServiceOfferingRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.utils.errorhandling.*;
import com.javaguru.appointmentbooking.utils.mapper.AppointmentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final AppointmentMapper mapper;

    public AppointmentResponse bookAppointment(UUID customerId, BookAppointmentRequest request) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("Customer not found."));

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new BusinessRuleViolationException(
                    "Selected branch is inactive."
            );
        }

        ServiceOffering serviceOffering = serviceOfferingRepository
                .findById(request.serviceOfferingId())
                .orElseThrow(() -> new ServiceOfferingNotFoundException("Service offering not found."));

        if (!Boolean.TRUE.equals(serviceOffering.getActive())) {
            throw new BusinessRuleViolationException(
                    "Selected service offering is inactive."
            );
        }

        LocalTime proposedEndTime = request.startTime()
                .plusMinutes(serviceOffering.getDurationMinutes());

        availabilityService.validateAppointmentDate(
                request.appointmentDate()
        );

        availabilityService.validateBusinessHours(
                request.startTime(),
                proposedEndTime
        );

        availabilityService.validateCustomerAvailability(
                customer.getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                null
        );

        User consultant = consultantAllocationService
                .allocateConsultant(
                        branch.getId(),
                        request.appointmentDate(),
                        request.startTime(),
                        serviceOffering.getDurationMinutes()
                );

        availabilityService.validateConsultantAvailability(
                consultant.getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                null
        );


        Appointment appointment =new Appointment();
        appointment.setAppointmentNumber(generateAppointmentNumber());
        appointment.setCustomer(customer);
        appointment.setConsultant(consultant);
        appointment.setBranch(branch);
        appointment.setServiceOffering(serviceOffering);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(proposedEndTime);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setNotes(request.notes());


        try {

            Appointment saved = appointmentRepository.save(appointment);
            notificationService.createAppointmentBookedNotification(saved);

            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateAppointmentException(
                    "Selected appointment slot is no longer available.");
        }
    }

    public AppointmentResponse getAppointment(UUID appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found."));

        return mapper.toResponse(appointment);
    }

    public Page<AppointmentResponse> getMyAppointments(
            Authentication authentication, Pageable pageable) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Page<AppointmentResponse> appointments;

        if (user.getRole().equals(Role.ROLE_CONSULTANT)) {

            appointments = appointmentRepository
                    .findByConsultantIdOrderByAppointmentDateAscStartTimeAsc(
                            user.getId(),
                            pageable
                    )
                    .map(mapper::toResponse);

        } else if (user.getRole().equals(Role.ROLE_CUSTOMER)) {

            appointments = appointmentRepository
                    .findByCustomerId(
                            user.getId(),
                            pageable
                    )
                    .map(mapper::toResponse);
        } else {

            appointments = appointmentRepository
                    .findAll(pageable)
                    .map(mapper::toResponse);
        }

        return appointments;

    }

    public AppointmentResponse cancelAppointment(UUID appointmentId, Authentication authentication) {

        Appointment appointment = getAccessibleAppointment(appointmentId, authentication);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BusinessRuleViolationException(
                    "Completed appointments cannot be cancelled."
            );
        }
        validateTransition(
                appointment,
                AppointmentStatus.CANCELLED
        );
        appointment.setStatus(AppointmentStatus.CANCELLED);

        return mapper.toResponse(appointmentRepository.save(
                appointment)
        );
    }

    public AppointmentResponse rescheduleAppointment(
            UUID appointmentId,
            RescheduleAppointmentRequest request,
            Authentication authentication) {

        Appointment appointment = getAccessibleAppointment(appointmentId, authentication);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new BusinessRuleViolationException(
                    "Completed appointments cannot be rescheduled."
            );
        }

        LocalTime proposedEndTime = request.startTime().plusMinutes(
                appointment.getServiceOffering().getDurationMinutes());

        availabilityService.validateAppointmentDate(
                request.appointmentDate()
        );

        availabilityService.validateBusinessHours(
                request.startTime(),
                proposedEndTime
        );

        availabilityService.validateCustomerAvailability(
                appointment.getCustomer().getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                appointment.getId()
        );

        availabilityService.validateConsultantAvailability(
                appointment.getConsultant().getId(),
                request.appointmentDate(),
                request.startTime(),
                proposedEndTime,
                appointment.getId()
        );

        validateTransition(
                appointment,
                AppointmentStatus.RESCHEDULED
        );
        User consultant =
                consultantAllocationService
                        .allocateConsultant(
                                appointment.getBranch().getId(),
                                request.appointmentDate(),
                                request.startTime(),
                                appointment.getServiceOffering().getDurationMinutes()
                        );

        appointment.setConsultant(consultant);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.startTime()
                        .plusMinutes(appointment.getServiceOffering().getDurationMinutes())
        );

        return mapper.toResponse(
                appointmentRepository.save(appointment)
        );
    }

    public AppointmentResponse confirmAppointment(UUID appointmentId, Authentication authentication) {

        Appointment appointment = getAccessibleAppointment(appointmentId, authentication);
        validateTransition(
                appointment,
                AppointmentStatus.CONFIRMED
        );
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return mapper.toResponse(
                appointmentRepository.save(appointment)
        );
    }

    public AppointmentResponse completeAppointment(
            UUID appointmentId, Authentication authentication) {

        Appointment appointment = getAccessibleAppointment(appointmentId, authentication);

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessRuleViolationException(
                    "Only confirmed appointments can be completed."
            );
        }

        validateTransition(
                appointment,
                AppointmentStatus.COMPLETED
        );
        appointment.setStatus(AppointmentStatus.COMPLETED);

        return mapper.toResponse(appointmentRepository.save(appointment)
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

    public List<AppointmentResponse> getAppointmentsByStatus(UUID customerId, AppointmentStatus status) {

        return mapper.toResponseList(appointmentRepository
                .findByCustomerIdAndStatus(
                        customerId,
                        status
                ));
    }

    public List<AppointmentResponse> getAppointmentsByDateRange(UUID customerId, LocalDate fromDate, LocalDate toDate) {

        return mapper.toResponseList(appointmentRepository
                .findByCustomerIdAndAppointmentDateBetween(
                        customerId,
                        fromDate,
                        toDate
                ));
    }

    public List<AppointmentResponse> getUpcomingAppointments(UUID customerId) {

        return mapper.toResponseList(appointmentRepository
                .findByCustomerIdAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
                        customerId,
                        LocalDate.now()
                ));
    }

    public Page<AppointmentResponse> getConsultantAppointments(
            UUID consultantId, Pageable pageable) {

        return appointmentRepository
                .findByConsultantIdOrderByAppointmentDateAscStartTimeAsc(
                        consultantId,
                        pageable
                ).map(mapper::toResponse);
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

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    private Appointment getAccessibleAppointment(UUID appointmentId, Authentication authentication) {
        Appointment appointment;
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user.getRole() == Role.ROLE_CUSTOMER) {

            appointment = appointmentRepository
                    .findByIdAndCustomerId(
                            appointmentId,
                            user.getId()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        } else if (user.getRole() == Role.ROLE_CONSULTANT) {

            appointment = appointmentRepository
                    .findByIdAndConsultantId(
                            appointmentId,
                            user.getId()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        } else {
            appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found."));
        }
        return appointment;
    }

    private void validateTransition(Appointment appointment, AppointmentStatus targetStatus) {

        if (!appointment.getStatus().canTransitionTo(targetStatus)) {

            throw new BusinessRuleViolationException(
                    String.format(
                            "Appointment cannot transition from %s to %s",
                            appointment.getStatus(),
                            targetStatus
                    )
            );
        }

    }
}