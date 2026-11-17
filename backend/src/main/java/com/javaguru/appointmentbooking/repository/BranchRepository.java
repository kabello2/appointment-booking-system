package com.javaguru.appointmentbooking.repository;

import com.javaguru.appointmentbooking.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    boolean existsByBranchCode(String branchCode);
    long countByActiveTrue();
}
