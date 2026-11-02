
package com.foodies.freshmeal.authentication.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.foodies.freshmeal.common.config.FrontendProperties;

/**
 * ============================================================================
 * Component : PasswordResetUrlBuilder
 * ============================================================================
 *
 * Constructs frontend URLs used during password recovery.
 *
 * Responsibilities:
 * - Resolve the configured frontend application URL.
 * - Append the password-reset page path.
 * - Attach the raw password-reset token as a query parameter.
 * - Validate URL configuration before constructing the final URL.
 *
 * Security:
 * - Never persists the raw password-reset token.
 * - Never logs the generated password-reset URL.
 * - Encodes query parameters to prevent URL construction errors.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class PasswordResetUrlBuilder {

    // =========================================================================
    // Dependencies
    // =========================================================================

    /**
     * Centralized FreshMeal frontend configuration.
     */
    private final FrontendProperties frontendProperties;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Creates the password-reset URL builder.
     *
     * @param frontendProperties frontend application configuration.
     */
    public PasswordResetUrlBuilder(final FrontendProperties frontendProperties) {
        this.frontendProperties = frontendProperties;
    }

    // =========================================================================
    // URL Construction
    // =========================================================================

    /**
     * Builds the frontend password-reset URL.
     *
     * @param rawResetToken raw password-reset token.
     * @return complete frontend password-reset URL.
     *
     * @throws IllegalArgumentException when the token or URL configuration
     *                                  is invalid.
     */
    public String buildResetUrl(final String rawResetToken) {

        if (!StringUtils.hasText(rawResetToken)) {
            throw new IllegalArgumentException(
                    "Password reset token must not be empty.");
        }

        final String baseUrl = normalizeBaseUrl(
                frontendProperties.getBaseUrl());

        final String resetPath = normalizeResetPath(
                frontendProperties.getPasswordResetPath());

        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(resetPath)
                .queryParam("token", rawResetToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }

    // =========================================================================
    // Configuration Validation
    // =========================================================================

    /**
     * Validates and normalizes the frontend base URL.
     *
     * Only absolute HTTP and HTTPS URLs are accepted.
     *
     * @param configuredBaseUrl configured frontend base URL.
     * @return normalized frontend base URL.
     */
    private String normalizeBaseUrl(final String configuredBaseUrl) {

        if (!StringUtils.hasText(configuredBaseUrl)) {
            throw new IllegalArgumentException(
                    "Frontend base URL must not be configured.");
        }

        final String baseUrl = configuredBaseUrl.trim();

        final URI uri;

        try {
            uri = URI.create(baseUrl);
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Frontend base URL is invalid.",
                    exception);
        }

        final String scheme = uri.getScheme();

        if (!uri.isAbsolute()
                || !StringUtils.hasText(uri.getHost())
                || scheme == null
                || !("http".equalsIgnoreCase(scheme)
                        || "https".equalsIgnoreCase(scheme))
                || uri.getRawUserInfo() != null
                || uri.getRawQuery() != null
                || uri.getRawFragment() != null) {

            throw new IllegalArgumentException(
                    "Frontend base URL must be an absolute HTTP or HTTPS URL without credentials, query parameters or fragments.");
        }

        // Remove trailing slashes to avoid duplicate separators.
        return baseUrl.replaceAll("/+$", "");
    }

    /**
     * Validates and normalizes the frontend password-reset path.
     *
     * @param configuredResetPath configured password-reset page path.
     * @return normalized password-reset path.
     */
    private String normalizeResetPath(final String configuredResetPath) {

        if (!StringUtils.hasText(configuredResetPath)) {
            throw new IllegalArgumentException(
                    "Frontend password-reset path must not be empty.");
        }

        final String resetPath = configuredResetPath.trim();

        if (resetPath.contains("?")
                || resetPath.contains("#")
                || resetPath.contains("://")) {

            throw new IllegalArgumentException(
                    "Frontend password-reset path must contain only a valid application path.");
        }

        return resetPath.startsWith("/")
                ? resetPath
                : "/" + resetPath;
    }
}
