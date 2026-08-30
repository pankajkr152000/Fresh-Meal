package com.foodies.freshmeal.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * ============================================================================
 * Configuration : OpenAPI
 * ============================================================================
 *
 * <p>
 * Provides the global OpenAPI configuration for the FreshMeal REST API.
 * </p>
 *
 * <p>
 * This configuration defines application-level API metadata and the common
 * authentication scheme used by documented APIs.
 * </p>
 *
 * <p>
 * Module-specific endpoint documentation remains within the respective
 * controllers so that each domain module remains responsible for its own
 * API contract.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "FreshMeal API", version = "1.0", description = """
        REST API for the FreshMeal food ordering platform.

        The API is organized into independent domain modules
        such as Food, Restaurant, Restaurant Branch, Order,
        User, Payment, Delivery Partner, and other supporting
        modules.
        """, contact = @Contact(name = "FreshMeal Development Team")), security = {
        @SecurityRequirement(name = "bearerAuth")
})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

    /**
     * Creates the global OpenAPI definition.
     *
     * <p>
     * The bean is intentionally kept lightweight because springdoc handles
     * controller and DTO discovery automatically.
     * </p>
     *
     * @return global OpenAPI configuration
     */
    @Bean
    public OpenAPI freshMealOpenAPI() {
        return new OpenAPI();
    }
}