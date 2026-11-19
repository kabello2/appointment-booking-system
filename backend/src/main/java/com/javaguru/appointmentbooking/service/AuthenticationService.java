package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.config.JwtService;
import com.javaguru.appointmentbooking.dto.request.ChangePasswordRequest;
import com.javaguru.appointmentbooking.dto.request.LoginRequest;
import com.javaguru.appointmentbooking.dto.request.CreateUserRequest;
import com.javaguru.appointmentbooking.dto.response.AuthenticationResponse;
import com.javaguru.appointmentbooking.dto.response.UserResponse;
import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.repository.UserRepository;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.utils.errorhandling.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(
            CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    String.format(
                            "User with email [%s] already exists.",
                            request.email()
                    )
            );
        }

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());

        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        user.setRole(Role.ROLE_CUSTOMER);

        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setEmailVerified(false);
        user.setFailedLoginAttempts(0);

        User savedUser = userRepository.save(user);

        log.info(
                "Customer registered successfully. Email={}",
                savedUser.getEmail()
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRole(),
                savedUser.isEnabled(),
                savedUser.getBranch() != null ? savedUser.getBranch().getId() : null
        );
    }
    public AuthenticationResponse login(
            LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password."
                        ));

        checkAccountEligibility(user);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            processFailedLogin(user);

            throw new InvalidCredentialsException(
                    "Invalid email or password."
            );
        }

        resetFailedAttempts(user);

        String token =
                jwtService.generateToken(user);

        return new AuthenticationResponse(
                token,
                "Bearer",
                jwtService.getAccessTokenExpiryInSeconds()
        );
    }

    @Transactional
    public void changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        )
                );

        boolean matches =
                passwordEncoder.matches(
                        request.currentPassword(),
                        user.getPasswordHash()
                );

        if (!matches) {

            throw new BusinessRuleViolationException(
                    "Current password is incorrect."
            );
        }

        if (request.currentPassword()
                .equals(request.newPassword())) {

            throw new BusinessRuleViolationException(
                    "New password must be different from current password."
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.newPassword()
                )
        );

        user.setFailedLoginAttempts(0);

        user.setAccountLocked(false);

        user.setLockedUntil(null);

        userRepository.save(user);

        log.info(
                "Password changed successfully for user {}",
                user.getEmail()
        );
    }

    private void checkAccountEligibility(User user) {

        if (!user.isEnabled()) {

            throw new AccountDisabledException(
                    "Account is disabled."
            );
        }

        if (user.isAccountLocked()) {

            if (user.getLockedUntil() != null
                    && user.getLockedUntil()
                    .isAfter(LocalDateTime.now())) {

                throw new AccountLockedException(
                        String.format(
                                "Account locked until %s",
                                user.getLockedUntil()
                        )
                );
            }

            user.setAccountLocked(false);
            user.setLockedUntil(null);

            userRepository.save(user);
        }
    }

    private void processFailedLogin(User user) {

        int attempts =
                user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(attempts);

        if (attempts >= 5) {

            user.setAccountLocked(true);

            user.setLockedUntil(
                    LocalDateTime.now().plusMinutes(30)
            );

            log.warn(
                    "Account locked due to failed login attempts. Email={}",
                    user.getEmail()
            );
        }

        userRepository.save(user);
    }

    private void resetFailedAttempts(User user) {

        if (user.getFailedLoginAttempts() > 0) {

            user.setFailedLoginAttempts(0);

            user.setLockedUntil(null);

            userRepository.save(user);
        }
    }
}