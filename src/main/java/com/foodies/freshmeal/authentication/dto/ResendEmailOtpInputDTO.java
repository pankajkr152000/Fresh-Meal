package com.foodies.freshmeal.authentication.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * Resend Email OTP Input DTO
 * ============================================================================
 *
 * Service-layer input wrapper for the email verification OTP resend workflow.
 *
 * <p>
 * This DTO separates the external API request model from the internal service
 * input model and follows the standard FreshMeal service input pattern.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
public class ResendEmailOtpInputDTO {

    /**
     * API request containing the email address for OTP resend.
     */
    private ResendEmailOtpRequest resendEmailOtpRequest;
}