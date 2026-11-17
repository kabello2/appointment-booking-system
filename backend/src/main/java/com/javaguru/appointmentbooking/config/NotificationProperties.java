package com.javaguru.appointmentbooking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification")
public record NotificationProperties(
        Dispatcher dispatcher,
        Integer batchSize
) {
    public record Dispatcher(
            Long fixedDelayMs
    ) { }
}