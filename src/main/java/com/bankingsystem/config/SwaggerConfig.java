package com.bankingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    // Modified: Swagger JWT Configuration (30-06-2026)
    @Bean
     OpenAPI openAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                // API Information
                .info(new Info()
                        .title("Banking Management System API")
                        .version("1.0")
                        .description("REST APIs for Banking Management System using Spring Boot, JWT Authentication, Spring Security and MySQL"))

                // Global Security Requirement
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                // JWT Configuration
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)
                                )
                );
    }
}