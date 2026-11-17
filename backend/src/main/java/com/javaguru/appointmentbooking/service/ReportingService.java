package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.response.*;
import com.javaguru.appointmentbooking.entity.enums.AppointmentStatus;
import com.javaguru.appointmentbooking.entity.enums.NotificationStatus;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportingService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;

    public DashboardSummaryResponse getDashboardSummary() {

        return new DashboardSummaryResponse(
                userRepository.countByRole(Role.ROLE_CUSTOMER),
                userRepository.countByRole(Role.ROLE_CONSULTANT),
                branchRepository.countByActiveTrue(),
                serviceOfferingRepository.countByActiveTrue(),
                appointmentRepository.count(),
                appointmentRepository.countByAppointmentDate(LocalDate.now()),
                appointmentRepository.countByStatus(AppointmentStatus.COMPLETED),
                notificationRepository.countByStatus(NotificationStatus.PENDING)
        );
    }

    public AppointmentStatusSummaryResponse getAppointmentStatusSummary() {

        return new AppointmentStatusSummaryResponse(
                appointmentRepository.countByStatus(AppointmentStatus.BOOKED),
                appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED),
                appointmentRepository.countByStatus(AppointmentStatus.COMPLETED),
                appointmentRepository.countByStatus(AppointmentStatus.CANCELLED),
                appointmentRepository.countByStatus(AppointmentStatus.RESCHEDULED)
        );
    }

    @Transactional
    public List<BranchAppointmentReportResponse> getAppointmentCountPerBranch() {

        return appointmentRepository
                .getAppointmentCountPerBranch()
                .stream()
                .map(result ->
                        new BranchAppointmentReportResponse(
                                result.getBranchId(),
                                result.getBranchCode(),
                                result.getBranchName(),
                                result.getAppointmentCount()
                        )
                )
                .toList();
    }

    @Transactional
    public List<ServicePopularityResponse> getServicePopularity() {

        return appointmentRepository
                .getServicePopularity()
                .stream()
                .map(result ->
                        new ServicePopularityResponse(
                                result.getServiceId(),
                                result.getServiceCode(),
                                result.getServiceName(),
                                result.getBookingCount()
                        )
                )
                .toList();
    }

    @Transactional
    public List<DailyAppointmentReportResponse> getAppointmentCountPerDay() {

        return appointmentRepository
                .getAppointmentCountPerDay()
                .stream()
                .map(result ->
                        new DailyAppointmentReportResponse(
                                result.getAppointmentDate(),
                                result.getAppointmentCount()
                        )
                )
                .toList();
    }

    @Transactional
    public List<MonthlyAppointmentReportResponse> getAppointmentCountPerMonth() {

        return appointmentRepository
                .getAppointmentCountPerMonth()
                .stream()
                .map(result ->
                        new MonthlyAppointmentReportResponse(
                                result.getYear(),
                                result.getMonth(),
                                result.getAppointmentCount()
                        )
                )
                .toList();
    }

    @Transactional
    public List<ConsultantWorkloadResponse> getConsultantWorkload() {

        return appointmentRepository
                .getConsultantWorkload()
                .stream()
                .map(result ->
                        new ConsultantWorkloadResponse(
                                result.getConsultantId(),
                                result.getFirstName(),
                                result.getLastName(),
                                result.getAppointmentCount()
                        )
                )
                .toList();
    }

}