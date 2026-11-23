package com.javaguru.appointmentbooking.service;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginFailureService {

    private final UserRepository userRepository;

    @Value("${security.login.max-failed-attempts}")
    private int maxFailedAttempts;

    @Value("${security.login.lock-duration-minutes}")
    private long lockDurationMinutes;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void recordFailedLogin(User user) {

        int failedAttempts = user.getFailedLoginAttempts() + 1;

        user.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= maxFailedAttempts) {

            user.setAccountLocked(true);
            user.setLockedUntil(
                    LocalDateTime.now()
                            .plusMinutes(lockDurationMinutes)
            );
        }

        userRepository.save(user);

    }

}
