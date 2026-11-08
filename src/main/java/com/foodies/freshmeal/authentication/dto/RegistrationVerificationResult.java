
package com.foodies.freshmeal.authentication.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the outcome of pending registration verification.
 *
 * <p>
 * Contains the verification status, remaining OTP attempts, and permanent
 * registration details when verification succeeds.
 * </p>
 *
 * <p>
 * An unsuccessful result allows the transactional verification workflow
 * to commit failed OTP attempt updates without triggering transaction
 * rollback.
 * </p>
 *
 * <p>
 * Registration details are populated only after successful permanent
 * user creation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class RegistrationVerificationResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether registration verification succeeded.
     */
    private final boolean verified;

    /**
     * Number of remaining OTP verification attempts.
     */
    private final int attemptsRemaining;

    /**
     * Permanent registration details.
     *
     * <p>
     * Null when verification is unsuccessful.
     * </p>
     */
    private final VerifiedRegistrationResult registration;
}
