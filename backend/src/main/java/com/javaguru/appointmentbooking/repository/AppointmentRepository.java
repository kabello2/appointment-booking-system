package com.javaguru.appointmentbooking.repository;

import com.javaguru.appointmentbooking.entity.Appointment;
import com.javaguru.appointmentbooking.entity.enums.AppointmentStatus;
import com.javaguru.appointmentbooking.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByCustomerId(UUID customerId);

    @Query("""
       SELECT a
       FROM Appointment a
       WHERE a.branch.id = :branchId
       AND a.appointmentDate = :appointmentDate
       AND a.status <> com.javaguru.appointmentbooking.entity.enums.AppointmentStatus.CANCELLED
       """)
    List<Appointment> findActiveAppointmentsForBranch(
            @Param("branchId") UUID branchId,
            @Param("appointmentDate") LocalDate appointmentDate);

    List<Appointment> findByBranchIdAndAppointmentDate(
            UUID branchId,
            LocalDate appointmentDate
    );

    long countByStatus(AppointmentStatus status);
    long countByAppointmentDate(LocalDate appointmentDate);
    long count();
    @Query("""
       SELECT
            b.id AS branchId,
            b.branchCode AS branchCode,
            b.name AS branchName,
            COUNT(a.id) AS appointmentCount
       FROM Appointment a
       JOIN a.branch b
       GROUP BY
            b.id,
            b.branchCode,
            b.name
       ORDER BY COUNT(a.id) DESC
       """)
    List<BranchAppointmentReportProjection> getAppointmentCountPerBranch();


    @Query("""
       SELECT
            s.id AS serviceId,
            s.serviceCode AS serviceCode,
            s.name AS serviceName,
            COUNT(a.id) AS bookingCount
       FROM Appointment a
       JOIN a.serviceOffering s
       GROUP BY
            s.id,
            s.serviceCode,
            s.name
       ORDER BY COUNT(a.id) DESC
       """)
    List<ServicePopularityProjection> getServicePopularity();

    @Query("""
       SELECT
            a.appointmentDate AS appointmentDate,
            COUNT(a.id) AS appointmentCount
       FROM Appointment a
       GROUP BY a.appointmentDate
       ORDER BY a.appointmentDate DESC
       """)
    List<DailyAppointmentProjection> getAppointmentCountPerDay();

    @Query("""
       SELECT
            YEAR(a.appointmentDate) AS year,
            MONTH(a.appointmentDate) AS month,
            COUNT(a.id) AS appointmentCount
       FROM Appointment a
       GROUP BY
            YEAR(a.appointmentDate),
            MONTH(a.appointmentDate)
       ORDER BY
            YEAR(a.appointmentDate) DESC,
            MONTH(a.appointmentDate) DESC
       """)
    List<MonthlyAppointmentProjection> getAppointmentCountPerMonth();

    @Query("""
       SELECT
            c.id AS consultantId,
            c.firstName AS firstName,
            c.lastName AS lastName,
            COUNT(a.id) AS appointmentCount
       FROM Appointment a
       JOIN a.consultant c
       GROUP BY
            c.id,
            c.firstName,
            c.lastName
       ORDER BY COUNT(a.id) DESC
       """)
    List<ConsultantWorkloadProjection> getConsultantWorkload();

    @Query("""
       SELECT a
       FROM Appointment a
       WHERE a.consultant.id = :consultantId
       AND a.appointmentDate = :appointmentDate
       AND a.status <> com.javaguru.appointmentbooking.entity.enums.AppointmentStatus.CANCELLED
       """)
    List<Appointment> findActiveAppointmentsForConsultant(
            @Param("consultantId") UUID consultantId,
            @Param("appointmentDate") LocalDate appointmentDate
    );

    @Query("""
       SELECT COUNT(a)
       FROM Appointment a
       WHERE a.consultant.id = :consultantId
       AND a.appointmentDate = :appointmentDate
       AND a.status <> com.javaguru.appointmentbooking.entity.enums.AppointmentStatus.CANCELLED
       """)
    long getConsultantActiveWorkload(
            @Param("consultantId") UUID consultantId,
            @Param("appointmentDate") LocalDate appointmentDate
    );

    List<Appointment> findByCustomerIdAndStatus(
            UUID customerId,
            AppointmentStatus status
    );

    List<Appointment> findByCustomerIdAndAppointmentDateBetween(
            UUID customerId,
            LocalDate fromDate,
            LocalDate toDate
    );

    Optional<Appointment> findByIdAndConsultantId(
            UUID appointmentId,
            UUID consultantId
    );

    List<Appointment>
    findByCustomerIdAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
            UUID customerId,
            LocalDate appointmentDate
    );

    Optional<Appointment> findByIdAndCustomerId(
            UUID appointmentId,
            UUID customerId
    );

    List<Appointment>
    findByConsultantIdAndAppointmentDate(
            UUID consultantId,
            LocalDate appointmentDate
    );

    List<Appointment>
    findByConsultantIdOrderByAppointmentDateAscStartTimeAsc(
            UUID consultantId
    );

    long countByCustomerIdAndStatus(
            UUID customerId,
            AppointmentStatus status
    );

    @Query("""
       SELECT COUNT(a)
       FROM Appointment a
       WHERE a.customer.id = :customerId
       AND a.appointmentDate >= :today
       """)
    long countUpcomingAppointments(
            UUID customerId,
            LocalDate today
    );

    @Query("""
       SELECT a
       FROM Appointment a
       WHERE a.customer.id = :customerId
       AND a.appointmentDate = :appointmentDate
       AND a.status <> com.javaguru.appointmentbooking.entity.enums.AppointmentStatus.CANCELLED
       """)
    List<Appointment> findActiveAppointmentsForCustomer(
            UUID customerId,
            LocalDate appointmentDate
    );

}