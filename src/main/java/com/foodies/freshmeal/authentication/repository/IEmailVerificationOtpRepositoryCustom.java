
package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.foodies.freshmeal.authentication.entity.EmailVerificationOtpEntity;

/**
 * Defines atomic persistence operations for email verification OTPs.
 *
 * <p>
 * These operations enforce OTP lifecycle transitions at the database
 * level to protect against concurrent requests.
 * </p>
 */
public interface IEmailVerificationOtpRepositoryCustom {

    /**
     * Atomically consumes an eligible OTP.
     *
     * @param verificationNumber unique OTP verification number
     * @param now                current business timestamp
     * @param updatedBy          actor performing the operation
     * @return the consumed OTP when the conditional update succeeds;
     *         otherwise empty
     */
    Optional<EmailVerificationOtpEntity> consumeEmailVerificationOtp(
            String verificationNumber,
            LocalDateTime now,
            String updatedBy);

    /**
     * Atomically increments the failed verification attempt count.
     *
     * <p>
     * When the configured maximum is reached, the operation also
     * establishes the attempt lockout timestamp.
     * </p>
     *
     * @param verificationNumber unique OTP verification number
     * @param maxAttempts        maximum permitted verification attempts
     * @param now                current business timestamp
     * @param updatedBy          actor performing the operation
     * @return the updated OTP when the operation succeeds;
     *         otherwise empty
     */
    Optional<EmailVerificationOtpEntity> incrementFailedAttempt(
            String verificationNumber,
            int maxAttempts,
            LocalDateTime now,
            String updatedBy);

    /**
     * Records the first successful dispatch timestamp for an OTP.
     *
     * <p>
     * An existing dispatch timestamp must never be overwritten.
     * </p>
     *
     * @param verificationNumber unique OTP verification number
     * @param sentAt             successful dispatch timestamp
     * @param updatedBy          actor performing the operation
     * @return the updated OTP when the operation succeeds;
     *         otherwise empty
     */
    Optional<EmailVerificationOtpEntity> markEmailVerificationOtpSent(
            String verificationNumber,
            LocalDateTime sentAt,
            String updatedBy);

    /**
     * Revokes all active OTPs associated with an existing user.
     *
     * @param userNumber unique user number
     * @param revokedAt  revocation timestamp
     * @param updatedBy  actor performing the operation
     * @return number of OTP records revoked
     */
    long revokeActiveOtpsByUserNumber(
            String userNumber,
            LocalDateTime revokedAt,
            String updatedBy);

    /**
     * Revokes all active OTPs associated with a pending registration.
     *
     * @param registrationNumber unique pending registration number
     * @param revokedAt          revocation timestamp
     * @param updatedBy          actor performing the operation
     * @return number of OTP records revoked
     */
    long revokeActiveOtpsByRegistrationNumber(
            String registrationNumber,
            LocalDateTime revokedAt,
            String updatedBy);
}
