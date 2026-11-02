
package com.foodies.freshmeal.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * Configuration : FrontendProperties
 * ============================================================================
 *
 * Provides centralized configuration for the FreshMeal frontend application.
 *
 * Responsibilities:
 * - Maintain the frontend application base URL.
 * - Maintain frontend page paths used by backend workflows.
 * - Provide type-safe access to frontend configuration.
 *
 * Configuration prefix: freshmeal.frontend
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "freshmeal.frontend")
public class FrontendProperties {

    // =========================================================================
    // Frontend Configuration
    // =========================================================================

    /**
     * Base URL of the FreshMeal frontend application.
     */
    private String baseUrl;

    /**
     * Frontend path used for password recovery.
     */
    private String passwordResetPath;

    // =========================================================================
    // Getters / Setters
    // =========================================================================

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPasswordResetPath() {
        return passwordResetPath;
    }

    public void setPasswordResetPath(final String passwordResetPath) {
        this.passwordResetPath = passwordResetPath;
    }
}
