package ru.gw3nax.tickettrackerwebsite.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Flight Tracker API",
                version = "1.0",
                description = "API documentation for the Flight Tracker application"
        )
)
public class OpenApiConfig {
}