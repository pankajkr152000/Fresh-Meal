package com.foodies.freshmeal.authentication.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;

/**
 * ============================================================================
 * Email Verification OTP Entity
 * ============================================================================
 *
 * MongoDB persistence entity representing an email verification OTP issued
 * during FreshMeal account registration.
 *
 * <p>
 * This entity manages the lifecycle of an email verification OTP independently
 * from {@link com.foodies.freshmeal.user.entity.UserEntity}. The user entity
 * represents the account, while this entity represents the temporary
 * verification mechanism used to prove ownership of the registered email
 * address.
 * </p>
 *
 * <h3>Security</h3>
 *
 * <ul>
 * <li>The raw OTP must never be persisted.</li>
 * <li>Only a secure hash of the OTP is stored.</li>
 * <li>Each OTP has a limited lifetime.</li>
 * <li>Each OTP has a limited number of verification attempts.</li>
 * <li>A successfully verified OTP becomes unusable.</li>
 * <li>When a new OTP is issued, the previous active OTP is revoked.</li>
 * </ul>
 *
 * <h3>Lifecycle</h3>
 *
 * <pre>
 * Generate OTP
 *      ↓
 * Store OTP hash
 *      ↓
 * Send OTP by email
 *      ↓
 * Verify OTP
 *      ↓
 * ┌───────────────┬────────────────┐
 * │ Valid         │ Invalid        │
 * ↓               ↓                │
 * Mark used       Increment        │
 *                 attempts         │
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
@Document(collection = "fm_email_verification_otps")
public class EmailVerificationOtpEntity extends ABaseEntity {

    /**
     * Business identifier assigned to the OTP verification record.
     */
    @Indexed(unique = true)
    private String verificationNumber;

    /**
     * FreshMeal user number associated with this verification request.
     */
    @Indexed
    private String userNumber;

    /**
     * Email address for which the OTP was generated.
     */
    @Indexed
    private String email;

    /**
     * Secure hash of the generated OTP.
     *
     * <p>
     * The raw OTP must never be persisted.
     * </p>
     */
    private String otpHash;

    /**
     * Time at which the verification OTP was sent to the user's email address.
     *
     * <p>
     * This is intentionally separate from {@code createdAt}. The persistence
     * record may be created before the email delivery attempt completes, while
     * {@code sentAt} represents the actual OTP dispatch event.
     * </p>
     */
    private LocalDateTime sentAt;

    /**
     * Time at which this OTP expires.
     */
    @Indexed
    private LocalDateTime expiresAt;

    /**
     * Number of verification attempts already made.
     */
    private int attemptCount;

    /**
     * Maximum number of verification attempts permitted.
     */
    private int maxAttempts;

    /**
     * Indicates whether the OTP has been successfully consumed.
     */
    @Indexed
    private boolean used;

    /**
     * Time at which the OTP was successfully consumed.
     */
    private LocalDateTime usedAt;

    /**
     * Indicates whether the OTP was explicitly invalidated.
     */
    @Indexed
    private boolean revoked;

    /**
     * Time at which the OTP was revoked.
     */
    private LocalDateTime revokedAt;

    /**
     * Package-private constructor to enforce entity creation through the
     * established FreshMeal entity factory pattern.
     */
    EmailVerificationOtpEntity() {
        // Intentionally empty.
    }

    /**
     * Creates a new email verification OTP entity through the standard
     * FreshMeal entity factory.
     *
     * @return New email verification OTP entity.
     */
    public static IEntity create() {
        return new EmailVerificationOtpEntity();
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(final String verificationNumber) {
        this.verificationNumber = verificationNumber;
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

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(final String otpHash) {
        this.otpHash = otpHash;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(final LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(final int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(final int maxAttempts) {
        this.maxAttempts = maxAttempts;
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