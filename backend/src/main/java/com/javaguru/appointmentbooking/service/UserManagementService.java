package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.dto.request.AssignConsultantBranchRequest;
import com.javaguru.appointmentbooking.dto.request.CreateConsultantRequest;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.request.UpdateUserRequest;
import com.javaguru.appointmentbooking.dto.response.ConsultantResponse;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.entity.Branch;
import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.repository.BranchRepository;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.utils.errorhandling.BranchNotFoundException;
import com.javaguru.appointmentbooking.utils.errorhandling.BusinessRuleViolationException;
import com.javaguru.appointmentbooking.utils.errorhandling.UserAlreadyExistsException;
import com.javaguru.appointmentbooking.utils.errorhandling.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(
            CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {

            throw new UserAlreadyExistsException(
                    "User already exists."
            );
        }

        User user = new User();

        user.setRole(request.role());

        if (request.role() == Role.ROLE_CONSULTANT) {

            Branch branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

            user.setBranch(branch);
        }

        user.setFirstName(
                request.firstName());

        user.setLastName(
                request.lastName());

        user.setEmail(
                request.email());

        user.setPhoneNumber(
                request.phoneNumber());

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.password())
        );

        user.setEnabled(true);

        User saved =
                userRepository.save(user);

        return mapToResponse(saved);
    }

    @Transactional
    public void enableUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        )
                );

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Transactional
    public void disableUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        )
                );

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Transactional
    public UserResponse getUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format(
                                        "User with id [%s] not found.",
                                        userId
                                )
                        )
                );

        return mapToResponse(user);
    }

    @Transactional
    public List<UserResponse> getUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(
            UUID userId,
            UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                String.format(
                                        "User with id [%s] not found.",
                                        userId
                                )
                        )
                );

        if (!user.getEmail().equalsIgnoreCase(request.email())
                && userRepository.existsByEmail(request.email())) {

            throw new UserAlreadyExistsException(
                    String.format(
                            "User with email [%s] already exists.",
                            request.email()
                    )
            );
        }

        if (request.role() == Role.ROLE_CONSULTANT
                && (user.getBranch() != null && request.branchId() != null)
                && !user.getBranch().getId().equals(request.branchId())) {

            Branch branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

            user.setBranch(branch);

        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());

        User updatedUser =
                userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Transactional
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        return mapToResponse(user);
    }

    @Transactional
    public List<ConsultantResponse> getConsultants() {

        return userRepository.findByRole(Role.ROLE_CONSULTANT)
                .stream()
                .map(this::mapConsultantResponse)
                .toList();
    }

    @Transactional
    public List<ConsultantResponse>
    getConsultantsByBranch(UUID branchId) {

        return userRepository.findByBranchId(branchId)
                .stream()
                .filter(user ->
                        user.getRole() == Role.ROLE_CONSULTANT)
                .map(this::mapConsultantResponse)
                .toList();
    }

    @Transactional
    public ConsultantResponse assignBranch(
            UUID consultantId,
            AssignConsultantBranchRequest request) {

        User consultant = userRepository.findById(consultantId)
                .orElseThrow(() -> new UserNotFoundException("Consultant not found."));

        if (consultant.getRole() != Role.ROLE_CONSULTANT) {
            throw new BusinessRuleViolationException("User is not a consultant.");
        }

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch not found."));

        consultant.setBranch(branch);
        User saved = userRepository.save(
                        consultant);

        return mapConsultantResponse(saved);
    }

    @Transactional
    public User getUserEntityByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isEnabled(),
                user.getBranch() != null
                        ? user.getBranch().getId()
                        : null
        );
    }

    private ConsultantResponse mapConsultantResponse(User consultant) {

        return new ConsultantResponse(
                consultant.getId(),
                consultant.getFirstName(),
                consultant.getLastName(),
                consultant.getEmail(),
                consultant.getPhoneNumber(),
                consultant.isEnabled(),
                consultant.getBranch() != null
                        ? consultant.getBranch().getId()
                        : null,
                consultant.getBranch() != null
                        ? consultant.getBranch().getBranchCode()
                        : null,
                consultant.getBranch() != null
                        ? consultant.getBranch().getName()
                        : null
        );
    }
}