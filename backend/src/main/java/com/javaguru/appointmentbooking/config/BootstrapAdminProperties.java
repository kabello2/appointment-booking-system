package com.javaguru.appointmentbooking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "bootstrap.admin"
)
public record BootstrapAdminProperties(

        boolean enabled,

        String email,

        String password

) {
}
