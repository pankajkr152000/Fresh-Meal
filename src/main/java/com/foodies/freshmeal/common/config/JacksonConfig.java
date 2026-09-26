package com.foodies.freshmeal.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * ============================================================================
 * Configuration : JacksonConfig
 * ============================================================================
 *
 * Configures the application-wide Jackson {@link ObjectMapper}.
 *
 * <p>
 * The configured mapper is shared by FreshMeal components that require JSON
 * serialization and deserialization, including the audit framework.
 * </p>
 *
 * <p>
 * Java 8 date/time types such as {@link java.time.LocalDateTime},
 * {@link java.time.LocalDate}, {@link java.time.LocalTime}, and
 * {@link java.time.Instant} require the Jackson Java Time module.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates the application-wide Jackson {@link ObjectMapper}.
     *
     * <p>
     * The Java Time module enables Jackson to correctly serialize and
     * deserialize Java 8 date/time types used throughout the FreshMeal
     * application.
     * </p>
     *
     * @return configured application-wide ObjectMapper.
     */
    @Bean
    public ObjectMapper objectMapper() {

        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}