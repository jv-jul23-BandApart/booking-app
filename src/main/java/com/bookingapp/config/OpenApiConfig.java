package com.bookingapp.config;

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
    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes(
                        "Bearer Authentication", createApiKeyScheme()))
                .info(new Info().title("BOOKING API")
                        .description("The best application to plan your vacation!")
                        .version("1.0").contact(new Contact().name("Svyatoslav Tsytsyura")
                                .email("www.booking-app.com")
                                .url("tsytsiura.sviatoslav@gmail.com")));
    }
}
