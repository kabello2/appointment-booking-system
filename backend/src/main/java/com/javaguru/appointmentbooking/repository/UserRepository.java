package com.javaguru.appointmentbooking.repository;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByBranchId(UUID branchId);

    long countByRole(Role role);

    List<User> findByRoleAndBranchIdAndEnabled(
            Role role,
            UUID branchId,
            Boolean enabled
    );

    boolean existsByRole(Role role);
}