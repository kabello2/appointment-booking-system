package com.javaguru.appointmentbooking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "Bearer Authentication";
    @Bean
    public OpenAPI appointmentBookingApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Appointment Booking API")
                        .version("1.0.0")
                        .description("Branch Appointment Booking System")
                        .contact(new Contact().name("Admin")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME, new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}