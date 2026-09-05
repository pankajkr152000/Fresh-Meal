package com.foodies.freshmeal.authentication.dto;

/**
 * ============================================================================
 * Verify Email OTP Input DTO
 * ============================================================================
 *
 * Service input wrapper for the FreshMeal email verification operation.
 *
 * <p>
 * This DTO separates the externally supplied OTP verification request from
 * the common FreshMeal service-input infrastructure.
 * </p>
 *
 * <p>
 * The service context is intentionally not stored inside this DTO. It is
 * supplied through {@code IServiceInput}, consistent with the established
 * FreshMeal service architecture.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public class VerifyEmailOtpInputDTO {

    /**
     * Email verification request supplied by the client.
     */
    private VerifyEmailOtpRequest verifyEmailOtpRequest;

    /**
     * Returns the email verification request.
     *
     * @return Email verification request.
     */
    public VerifyEmailOtpRequest getVerifyEmailOtpRequest() {
        return verifyEmailOtpRequest;
    }

    /**
     * Sets the email verification request.
     *
     * @param verifyEmailOtpRequest Email verification request.
     */
    public void setVerifyEmailOtpRequest(final VerifyEmailOtpRequest verifyEmailOtpRequest) {
        this.verifyEmailOtpRequest = verifyEmailOtpRequest;
    }
}