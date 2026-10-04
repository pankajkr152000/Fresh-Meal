package com.foodies.freshmeal.checkout.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for checkout recovery operations.
 *
 * <p>
 * Centralizes recovery thresholds so that operational behavior can
 * be adjusted without modifying checkout business logic.
 * </p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "freshmeal.checkout.recovery")
public class CheckoutRecoveryProperties {

    /**
     * Maximum duration, in minutes, that a checkout may remain
     * in VALIDATING before it is considered stale.
     */
    @Min(value = 1, message = "Validation timeout must be at least one minute.")
    private long validationTimeoutMinutes = 10;
}