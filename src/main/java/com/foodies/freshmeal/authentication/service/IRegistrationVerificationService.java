package com.foodies.freshmeal.authentication.service;

import com.foodies.freshmeal.authentication.dto.RegistrationVerificationResult;
import com.foodies.freshmeal.common.io.service.IServiceContext;

/**
 * Defines operations for verifying pending user registrations.
 */
public interface IRegistrationVerificationService {

    /**
     * Verifies a pending registration and creates the permanent user account.
     *
     * <p>
     * OTP consumption, permanent user creation, and pending registration
     * completion must execute atomically within a MongoDB transaction.
     * </p>
     *
     * @param registrationNumber unique pending registration identifier
     * @param otp                email verification OTP
     * @param serviceContext     request execution context
     * @return verified registration details
     */
    RegistrationVerificationResult verifyAndCreateUser(
            String registrationNumber,
            String otp,
            IServiceContext serviceContext);
}