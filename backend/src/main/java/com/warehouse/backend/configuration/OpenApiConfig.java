package com.warehouse.backend.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Warehouse API", version = "1.0", description = "API for warehouse management")
)
@Configuration
public class OpenApiConfig {
}
