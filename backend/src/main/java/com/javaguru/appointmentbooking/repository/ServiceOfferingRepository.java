package com.javaguru.appointmentbooking.repository;

import com.javaguru.appointmentbooking.entity.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, UUID> {

    boolean existsByServiceCode(String serviceCode);
    List<ServiceOffering> findByActiveTrue();
    long countByActiveTrue();
}