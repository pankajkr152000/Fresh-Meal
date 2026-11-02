package com.foodies.freshmeal.authentication.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.foodies.freshmeal.authentication.entity.PasswordResetTokenEntity;

/**
 * ============================================================================
 * Password Reset Token Custom Repository
 * ============================================================================
 *
 * Defines authentication-specific atomic persistence operations for
 * password-reset tokens.
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IPasswordResetTokenRepositoryCustom {

    /**
     * Atomically claims a password-reset token.
     *
     * <p>
     * A token can only be claimed when it:
     * </p>
     *
     * <ul>
     * <li>Matches the supplied token hash.</li>
     * <li>Has not already been used.</li>
     * <li>Has not been revoked.</li>
     * <li>Has a recorded successful email dispatch.</li>
     * <li>Has not expired.</li>
     * <li>Belongs to an active record.</li>
     * </ul>
     *
     * <p>
     * A successful claim atomically marks the token as used.
     * </p>
     *
     * @param tokenHash Hash of the supplied raw reset token.
     * @param now       Current business-local application time.
     * @param updatedBy User or system identifier performing the operation.
     *
     * @return Claimed token, or empty if it cannot be claimed.
     */
    Optional<PasswordResetTokenEntity> claimPasswordResetToken(
            String tokenHash,
            LocalDateTime now,
            String updatedBy);
}