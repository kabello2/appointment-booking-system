package com.javaguru.appointmentbooking.config;

import com.javaguru.appointmentbooking.entity.User;
import com.javaguru.appointmentbooking.entity.enums.Role;
import com.javaguru.appointmentbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrapService
        implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BootstrapAdminProperties properties;

    @Override
    public void run(
            ApplicationArguments args
    ) {

        if (!properties.enabled()) {
            return;
        }

        boolean adminExists =
                userRepository.existsByRole(
                        Role.ROLE_ADMIN
                );

        if (adminExists) {
            return;
        }

        User admin = new User();
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setEmail(properties.email());
        admin.setPhoneNumber("+278218293");
        admin.setPasswordHash(passwordEncoder.encode(properties.password()));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setEnabled(true);
        admin.setEmailVerified(true);
        admin.setAccountLocked(false);
        admin.setFailedLoginAttempts(0);

        userRepository.save(admin);

    }
}