package com.suraev.TaskManagementSystem.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(
        title = "TaskManagementSystemApi",
        description = "Task management system", version = "0.0.1-SNAPSHOT",
        contact = @Contact(
                name = "Suraev Vitalij",
                email = "suraevvvitaly@gmail.com",
                url = "https://t.me/sh4dowb0lt"
        )
))
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
