
package com.foodies.freshmeal.authentication.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

/**
 * ============================================================================
 * Entity : PasswordResetTokenEntity
 * ============================================================================
 *
 * Represents a temporary password-reset authorization issued during the
 * FreshMeal password recovery process.
 *
 * <p>
 * This entity manages the lifecycle of a password reset token independently
 * from {@link com.foodies.freshmeal.user.entity.UserEntity}. The user entity
 * represents the account, while this entity represents the temporary security
 * credential that authorizes a password reset operation.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Associate a reset token with a FreshMeal user.</li>
 * <li>Store only a secure hash of the reset token.</li>
 * <li>Track token expiration.</li>
 * <li>Track whether the token has been consumed.</li>
 * <li>Support explicit token revocation.</li>
 * <li>Record successful reset-token dispatch.</li>
 * </ul>
 *
 * <h3>Security</h3>
 * <ul>
 * <li>The raw reset token must never be persisted.</li>
 * <li>Only a secure hash of the token is stored.</li>
 * <li>Reset tokens must have a limited lifetime.</li>
 * <li>A successfully used token becomes unusable.</li>
 * <li>Revoked tokens must not be accepted.</li>
 * </ul>
 *
 * <h3>Lifecycle</h3>
 *
 * <pre>
 * Password Recovery Request
 *          ↓
 * Generate Reset Token
 *          ↓
 * Store Token Hash
 *          ↓
 * Send Reset Instructions
 *          ↓
 * User Submits Reset Token
 *          ↓
 * Validate Token
 *          ↓
 * ┌─────────────────┬─────────────────┐
 * │ Valid           │ Invalid         │
 * ↓                 ↓                 │
 * Reset Password    Reject Request    │
 * ↓                                   │
 * Mark Token Used                     │
 * </pre>
 *
 * <p>
 * The entity extends {@link ABaseEntity} so that standard FreshMeal audit,
 * versioning, record-status, and soft-delete infrastructure is reused.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Document(collection = "fm_password_reset_tokens")
public class PasswordResetTokenEntity extends ABaseEntity {

    private static final long serialVersionUID = 619847302184756031L;

    // =========================================================================
    // Business Identifier
    // =========================================================================

    /**
     * Business-facing identifier assigned to the password reset record.
     *
     * <p>
     * This identifier is separate from the internal MongoDB document ID
     * inherited from {@link ABaseEntity}.
     * </p>
     */
    @Indexed(unique = true)
    private String resetNumber;

    // =========================================================================
    // User Reference
    // =========================================================================

    /**
     * FreshMeal business identifier of the user associated with this reset
     * request.
     */
    @Indexed
    private String userNumber;

    /**
     * Normalized email address associated with the password recovery request.
     */
    @Indexed
    private String email;

    // =========================================================================
    // Token Information
    // =========================================================================

    /**
     * Secure hash of the generated password reset token.
     *
     * <p>
     * The raw reset token must never be persisted.
     * </p>
     */
    private String tokenHash;

    /**
     * Time at which the reset token expires.
     */
    @Indexed
    private LocalDateTime expiresAt;

    // =========================================================================
    // Dispatch Information
    // =========================================================================

    /**
     * Time at which the password reset instructions were successfully
     * dispatched to the user's email address.
     *
     * <p>
     * This is intentionally separate from {@code createdAt}. The persistence
     * record and email delivery are two different events.
     * </p>
     */
    private LocalDateTime sentAt;

    // =========================================================================
    // Token Usage
    // =========================================================================

    /**
     * Indicates whether the reset token has already been successfully used.
     */
    @Indexed
    private boolean used;

    /**
     * Time at which the reset token was successfully used.
     */
    private LocalDateTime usedAt;

    // =========================================================================
    // Token Revocation
    // =========================================================================

    /**
     * Indicates whether the reset token has been explicitly invalidated.
     */
    @Indexed
    private boolean revoked;

    /**
     * Time at which the reset token was revoked.
     */
    private LocalDateTime revokedAt;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * <p>
     * Entity creation should happen through the established FreshMeal entity
     * factory pattern.
     * </p>
     */
    PasswordResetTokenEntity() {
        // Package-private constructor.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new {@link PasswordResetTokenEntity}.
     *
     * @return new password reset token entity.
     */
    public static IEntity create() {
        return new PasswordResetTokenEntity();
    }

    // =========================================================================
    // Getters / Setters
    // =========================================================================

    public String getResetNumber() {
        return resetNumber;
    }

    public void setResetNumber(final String resetNumber) {
        this.resetNumber = resetNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(final String userNumber) {
        this.userNumber = userNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(final String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(final LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(final boolean used) {
        this.used = used;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(final LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(final boolean revoked) {
        this.revoked = revoked;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(final LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
}