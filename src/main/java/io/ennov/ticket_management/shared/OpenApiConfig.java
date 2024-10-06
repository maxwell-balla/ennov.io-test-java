package io.ennov.ticket_management.shared;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiConfigLoader openApiConfigLoader;

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        try (InputStream inputStream = new ClassPathResource("api.yaml")
                .getInputStream()) {
            return openApiConfigLoader.load(inputStream);
        }
    }
}
