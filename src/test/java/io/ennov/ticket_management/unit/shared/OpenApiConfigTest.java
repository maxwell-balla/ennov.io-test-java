package io.ennov.ticket_management.unit.shared;

import io.ennov.ticket_management.shared.OpenApiConfig;
import io.ennov.ticket_management.shared.OpenApiConfigLoader;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @Mock
    private OpenApiConfigLoader openApiConfigLoader;

    @InjectMocks
    private OpenApiConfig openApiConfig;

    @Mock
    private OpenAPI mockOpenAPI;

    @BeforeEach
    void setUp() {
        System.setProperty("spring.config.location", "classpath:api.yaml");
    }

    @Test
    @DisplayName("Should load OpenAPI configuration from YAML file")
    void shouldLoadOpenAPIConfigurationFromYamlFile() throws IOException {
        // Given
        when(openApiConfigLoader.load(any(InputStream.class))).thenReturn(mockOpenAPI);

        // When
        OpenAPI result = openApiConfig.customOpenAPI();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(mockOpenAPI);
    }
}
