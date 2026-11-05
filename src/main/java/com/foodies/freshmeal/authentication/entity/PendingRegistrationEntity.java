
package com.foodies.freshmeal.authentication.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.constants.RegistrationStatus;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.valueObject.EmailAddress;
import com.foodies.freshmeal.common.valueObject.PhoneNumber;

import lombok.Getter;

/**
 * ============================================================================
 * Entity : PendingRegistrationEntity
 * ============================================================================
 *
 * MongoDB persistence entity representing a temporary FreshMeal registration
 * that has not yet been completed through successful email verification.
 *
 * <p>
 * This entity maintains registration information independently of
 * {@link com.foodies.freshmeal.user.entity.UserEntity}.
 * A permanent user account must never be created merely because registration
 * details are valid. Mandatory email verification must be completed first.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Maintain temporarily submitted registration information.</li>
 * <li>Maintain the requested public registration role.</li>
 * <li>Maintain the encoded password until registration completion.</li>
 * <li>Maintain the pending registration lifecycle.</li>
 * <li>Track registration expiration.</li>
 * <li>Track successful completion or cancellation.</li>
 * </ul>
 *
 * <h3>Business Identifier</h3>
 *
 * <p>
 * {@code registrationNumber} is the unique business-facing identifier
 * of a pending registration. It is used to associate email verification
 * records with the corresponding registration without requiring a
 * permanent user account.
 * </p>
 *
 * <h3>Password Security</h3>
 *
 * <ul>
 * <li>Only encoded passwords may be persisted.</li>
 * <li>Raw passwords must never be stored.</li>
 * <li>The encoded password must never be exposed through JSON
 * serialization.</li>
 * <li>The encoded password must never appear in application logs or audit
 * payloads.</li>
 * <li>The encoded password should be cleared after successful completion.</li>
 * </ul>
 *
 * <h3>Email Verification</h3>
 *
 * <p>
 * OTP generation, expiration, verification attempts, resend restrictions
 * and OTP consumption are managed independently by
 * {@link EmailVerificationOtpEntity}.
 * This entity does not maintain OTP-specific information.
 * </p>
 *
 * <h3>Registration Expiration</h3>
 *
 * <p>
 * A pending registration remains valid for 24 hours from its creation.
 * This validity period is independent of the email verification OTP
 * expiration period.
 * The expiration timestamp is calculated by the authentication service
 * and supplied during entity initialization.
 * </p>
 *
 * <h3>Lifecycle</h3>
 *
 * <pre>
 * PENDING_VERIFICATION
 *        |
 *        +------> COMPLETED
 *        |
 *        +------> EXPIRED
 *        |
 *        +------> CANCELLED
 * </pre>
 *
 * <p>
 * Terminal states cannot transition back into a pending state.
 * Lifecycle changes must happen through the dedicated entity methods.
 * </p>
 *
 * <h3>Persistence and Concurrency</h3>
 *
 * <p>
 * The entity extends {@link ABaseEntity} to reuse standard FreshMeal
 * persistence, audit, optimistic locking and soft-delete infrastructure.
 * The inherited version field supports optimistic concurrency control.
 * Database-level constraints and transactional service operations
 * must additionally protect registration completion.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Document(collection = "fm_pending_registrations")
public class PendingRegistrationEntity extends ABaseEntity {

    private static final long serialVersionUID = 7162948036518294057L;

    // =========================================================================
    // Registration Business Identifier
    // =========================================================================

    /**
     * Unique business-facing identifier of the pending registration.
     *
     * <p>
     * This identifier is used by registration and email verification
     * workflows instead of exposing the internal MongoDB identifier.
     * </p>
     */
    @Indexed(unique = true)
    private String registrationNumber;

    // =========================================================================
    // Registration Information
    // =========================================================================

    /**
     * Username requested during registration.
     *
     * <p>
     * The username is transferred to the permanent user account only after
     * successful email verification.
     * </p>
     */
    private String username;

    /**
     * First name supplied during registration.
     */
    private String firstName;

    /**
     * Last name supplied during registration.
     */
    private String lastName;

    /**
     * Normalized email address supplied during registration.
     *
     * <p>
     * Uses the common {@link EmailAddress} value object to maintain
     * consistent email representation across FreshMeal.
     * </p>
     */
    private EmailAddress email;

    /**
     * Phone number supplied during registration.
     *
     * <p>
     * Uses the common {@link PhoneNumber} value object to maintain
     * consistent contact information representation.
     * </p>
     */
    private PhoneNumber phoneNumber;

    // =========================================================================
    // Authentication Credentials
    // =========================================================================

    /**
     * Encoded password supplied during registration.
     *
     * <p>
     * The password is encoded by the authentication service before being
     * assigned to this entity.
     * </p>
     *
     * <p>
     * This field is intentionally excluded from JSON serialization.
     * It must never be exposed through API responses or application logs.
     * </p>
     */
    @JsonIgnore
    private String encodedPassword;

    // =========================================================================
    // Authorization
    // =========================================================================

    /**
     * Public registration role requested by the registrant.
     *
     * <p>
     * The role is assigned to the permanent user account only after
     * successful email verification and account creation.
     * </p>
     */
    private RoleType requestedRole;

    // =========================================================================
    // Registration Lifecycle
    // =========================================================================

    /**
     * Current lifecycle status of the pending registration.
     *
     * <p>
     * The initial state is
     * {@link RegistrationStatus#PENDING_VERIFICATION}.
     * Lifecycle transitions must happen through dedicated entity methods.
     * </p>
     */
    @Indexed
    private RegistrationStatus registrationStatus = RegistrationStatus.PENDING_VERIFICATION;

    /**
     * Time at which the pending registration expires.
     *
     * <p>
     * The default business validity period is 24 hours from registration
     * creation. OTP expiration is managed independently.
     * </p>
     */
    @Indexed
    private LocalDateTime expiresAt;

    /**
     * Time at which the registration was successfully completed.
     *
     * <p>
     * This field is populated only after successful email verification
     * and permanent account creation.
     * </p>
     */
    private LocalDateTime completedAt;

    /**
     * Time at which the registration was explicitly cancelled.
     */
    private LocalDateTime cancelledAt;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor to enforce entity creation through the
     * established FreshMeal entity factory pattern.
     */
    PendingRegistrationEntity() {
        // Intentionally empty.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new pending registration entity through the standard
     * FreshMeal entity factory.
     *
     * @return new pending registration entity.
     */
    public static IEntity create() {
        return new PendingRegistrationEntity();
    }

    // =========================================================================
    // Entity Initialization
    // =========================================================================

    /**
     * Initializes a new pending registration.
     *
     * <p>
     * Initialization is permitted only once. Registration information
     * cannot be replaced through this method after initialization.
     * </p>
     *
     * <p>
     * Registration expiration is calculated by the authentication service
     * using the configured FreshMeal business clock.
     * </p>
     *
     * @param registrationNumber unique business registration identifier.
     * @param username           requested username.
     * @param firstName          registrant's first name.
     * @param lastName           registrant's last name.
     * @param email              normalized email address.
     * @param phoneNumber        registrant's phone number.
     * @param encodedPassword    encoded password.
     * @param requestedRole      requested public registration role.
     * @param expiresAt          registration expiration timestamp.
     *
     * @throws BusinessException when registration information is invalid
     *                           or the entity has already been initialized.
     */
    public void initialize(
            final String registrationNumber,
            final String username,
            final String firstName,
            final String lastName,
            final EmailAddress email,
            final PhoneNumber phoneNumber,
            final String encodedPassword,
            final RoleType requestedRole,
            final LocalDateTime expiresAt) {

        if (this.registrationNumber != null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        if (registrationNumber == null || registrationNumber.isBlank()
                || username == null || username.isBlank()
                || email == null
                || email.getValue() == null
                || email.getValue().isBlank()
                || encodedPassword == null || encodedPassword.isBlank()
                || requestedRole == null
                || expiresAt == null) {

            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        this.registrationNumber = registrationNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.encodedPassword = encodedPassword;
        this.requestedRole = requestedRole;
        this.expiresAt = expiresAt;
        this.registrationStatus = RegistrationStatus.PENDING_VERIFICATION;
    }

    // =========================================================================
    // Lifecycle Utility Methods
    // =========================================================================

    /**
     * Checks whether the registration is currently pending verification.
     *
     * @return true when registration is pending; false otherwise.
     */
    public boolean isPending() {
        return RegistrationStatus.PENDING_VERIFICATION.equals(registrationStatus);
    }

    /**
     * Checks whether the pending registration has reached its expiration time.
     *
     * <p>
     * Expiration is evaluated against the supplied timestamp so that
     * business logic and unit tests use a consistent time reference.
     * </p>
     *
     * @param currentTime current business timestamp.
     * @return true when registration has expired; false otherwise.
     */
    public boolean isExpired(final LocalDateTime currentTime) {

        return currentTime != null
                && expiresAt != null
                && !currentTime.isBefore(expiresAt);
    }

    // =========================================================================
    // Lifecycle Transitions
    // =========================================================================

    /**
     * Marks the pending registration as successfully completed.
     *
     * <p>
     * Completion is permitted only while the registration is pending
     * and has not expired.
     * </p>
     *
     * <p>
     * The authentication service must ensure that permanent account
     * creation and this transition participate in the same transactional
     * workflow.
     * </p>
     *
     * @param completedAt successful registration completion timestamp.
     *
     * @throws BusinessException when registration is invalid, expired,
     *                           or cannot be completed in its current state.
     */
    public void completeRegistration(final LocalDateTime completedAt) {

        validatePendingState();

        if (completedAt == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        if (isExpired(completedAt)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_EXPIRED);
        }

        this.registrationStatus = RegistrationStatus.COMPLETED;
        this.completedAt = completedAt;
    }

    /**
     * Marks the pending registration as expired.
     *
     * <p>
     * An already expired registration is treated as an idempotent operation.
     * A registration cannot be expired before its expiration timestamp.
     * </p>
     *
     * @param currentTime current business timestamp.
     * @return true when the registration was transitioned to EXPIRED;
     *         false when it was already expired or has not yet expired.
     *
     * @throws BusinessException when current timestamp is invalid or
     *                           the registration has already reached another
     *                           terminal state.
     */
    public boolean expireRegistration(final LocalDateTime currentTime) {

        if (currentTime == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        if (RegistrationStatus.EXPIRED.equals(registrationStatus)) {
            return false;
        }

        validatePendingState();

        if (!isExpired(currentTime)) {
            return false;
        }

        this.registrationStatus = RegistrationStatus.EXPIRED;

        return true;
    }

    /**
     * Marks the pending registration as cancelled.
     *
     * <p>
     * Cancellation is permitted only while registration is pending
     * and has not expired.
     * </p>
     *
     * @param cancelledAt registration cancellation timestamp.
     *
     * @throws BusinessException when cancellation timestamp is invalid,
     *                           registration has expired, or registration is not
     *                           pending.
     */
    public void cancelRegistration(final LocalDateTime cancelledAt) {

        validatePendingState();

        if (cancelledAt == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        if (isExpired(cancelledAt)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_EXPIRED);
        }

        this.registrationStatus = RegistrationStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
    }

    // =========================================================================
    // Credential Management
    // =========================================================================

    /**
     * Clears the encoded password after successful registration completion.
     *
     * <p>
     * Removing the temporary credential reduces unnecessary retention of
     * authentication information in completed registration records.
     * </p>
     *
     * <p>
     * The operation is permitted only after registration reaches COMPLETED.
     * </p>
     *
     * @throws BusinessException when registration has not been completed.
     */
    public void clearEncodedPassword() {

        if (!RegistrationStatus.COMPLETED.equals(registrationStatus)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        this.encodedPassword = null;
    }

    // =========================================================================
    // Internal Validation
    // =========================================================================

    /**
     * Ensures that the registration is in the pending verification state.
     *
     * <p>
     * Provides a specific business error for each terminal lifecycle state.
     * </p>
     *
     * @throws BusinessException when registration is not pending.
     */
    private void validatePendingState() {

        if (registrationStatus == null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }

        switch (registrationStatus) {

            case COMPLETED:
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_ALREADY_COMPLETED);

            case EXPIRED:
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_EXPIRED);

            case CANCELLED:
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_ALREADY_CANCELLED);

            case PENDING_VERIFICATION:
                // Valid lifecycle state. Continue operation.
                break;

            default:
                throw new BusinessException(
                        AuthenticationErrorConstants.PENDING_REGISTRATION_INVALID);
        }
    }
}
