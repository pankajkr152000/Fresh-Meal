
package com.foodies.freshmeal.authentication.entity;

import java.io.Serial;
import java.util.Objects;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.authentication.constants.RegistrationIdentityOwnerType;
import com.foodies.freshmeal.authentication.constants.RegistrationIdentityReservationConstants;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.exception.BusinessException;

import lombok.Getter;

/**
 * ============================================================================
 * Entity : RegistrationIdentityReservationEntity
 * ============================================================================
 *
 * MongoDB persistence entity responsible for maintaining globally unique
 * identity reservations across pending registrations and permanent users.
 *
 * <p>
 * This entity acts as the authoritative uniqueness mechanism for:
 * </p>
 *
 * <ul>
 * <li>Username</li>
 * <li>Email address</li>
 * <li>Phone number</li>
 * </ul>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Maintain globally unique registration identity attributes.</li>
 * <li>Associate reserved identities with their current owner.</li>
 * <li>Maintain ownership during pending registration.</li>
 * <li>Transfer ownership after successful email verification.</li>
 * <li>Prevent unauthorized or repeated ownership transfers.</li>
 * <li>Preserve permanent user identity reservations.</li>
 * </ul>
 *
 * <h3>Ownership Model</h3>
 *
 * <pre>
 * PENDING_REGISTRATION
 *         |
 *         | Successful email verification
 *         | and permanent user creation
 *         v
 *        USER
 * </pre>
 *
 * <p>
 * A reservation initially belongs to a pending registration.
 * After successful email verification and permanent user creation,
 * ownership is transferred to the corresponding permanent user.
 * </p>
 *
 * <h3>Ownership Invariants</h3>
 *
 * <ul>
 * <li>PENDING_REGISTRATION: registrationNumber must be present
 * and userNumber must be null.</li>
 * <li>USER: userNumber must be present and registrationNumber
 * must be null.</li>
 * </ul>
 *
 * <h3>Identity Uniqueness</h3>
 *
 * <p>
 * Username, email and phone number are protected by independent
 * MongoDB unique indexes. These indexes prevent duplicate identities
 * across pending registrations and permanent users.
 * </p>
 *
 * <h3>Reservation Release</h3>
 *
 * <p>
 * Reservations belonging to expired or cancelled pending registrations
 * must be physically deleted through a conditional repository operation.
 * Soft deletion must not be used because unique indexes continue to
 * consider soft-deleted documents.
 * </p>
 *
 * <p>
 * Reservations belonging to permanent users must be retained even
 * when their accounts are disabled or soft-deleted.
 * </p>
 *
 * <h3>Persistence and Concurrency</h3>
 *
 * <p>
 * The entity extends {@link ABaseEntity} to reuse standard FreshMeal
 * persistence, audit, optimistic locking and soft-delete infrastructure.
 * </p>
 *
 * <p>
 * Database-level unique constraints and transactional service operations
 * additionally protect identity ownership and registration completion.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Document(collection = RegistrationIdentityReservationConstants.COLLECTION_NAME)
public class RegistrationIdentityReservationEntity extends ABaseEntity {

    @Serial
    private static final long serialVersionUID = 7162948036518294058L;

    // =========================================================================
    // Identity Information
    // =========================================================================

    /**
     * Globally unique username reserved by this entity.
     *
     * <p>
     * The username must use the same canonical representation as the
     * permanent user account.
     * </p>
     */
    @Indexed(name = "uk_registration_identity_username", unique = true)
    private String username;

    /**
     * Globally unique, normalized email address reserved by this entity.
     *
     * <p>
     * The stored value must use the same normalized representation
     * as the permanent user account.
     * </p>
     */
    @Indexed(name = "uk_registration_identity_email", unique = true)
    private String email;

    /**
     * Globally unique, canonical phone number reserved by this entity.
     *
     * <p>
     * The stored value must use the same canonical representation
     * as the permanent user account.
     * </p>
     */
    @Indexed(name = "uk_registration_identity_phone_number", unique = true)
    private String phoneNumber;

    // =========================================================================
    // Reservation Ownership
    // =========================================================================

    /**
     * Identifies the current owner of this identity reservation.
     *
     * <p>
     * The initial owner is PENDING_REGISTRATION. Ownership changes
     * to USER after successful permanent account creation.
     * </p>
     */
    private RegistrationIdentityOwnerType ownerType;

    /**
     * Business identifier of the pending registration owning this reservation.
     *
     * <p>
     * Required when ownerType is PENDING_REGISTRATION.
     * Must be null when ownerType is USER.
     * </p>
     */
    private String registrationNumber;

    /**
     * Business identifier of the permanent user owning this reservation.
     *
     * <p>
     * Required when ownerType is USER.
     * Must be null when ownerType is PENDING_REGISTRATION.
     * </p>
     */
    private String userNumber;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor to enforce entity creation through
     * the established FreshMeal entity factory pattern.
     */
    RegistrationIdentityReservationEntity() {
        // Intentionally empty.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new registration identity reservation entity through
     * the standard FreshMeal entity factory.
     *
     * @return new registration identity reservation entity.
     */
    public static IEntity create() {
        return new RegistrationIdentityReservationEntity();
    }

    // =========================================================================
    // Entity Initialization
    // =========================================================================

    /**
     * Initializes a new identity reservation for a pending registration.
     *
     * <p>
     * Initialization is permitted only once. Identity attributes and
     * ownership cannot be replaced through this method after initialization.
     * </p>
     *
     * <p>
     * The supplied identity values must already be normalized and
     * canonicalized by the registration service.
     * </p>
     *
     * @param username           normalized username.
     * @param email              normalized email address.
     * @param phoneNumber        canonical phone number.
     * @param registrationNumber pending registration business identifier.
     *
     * @throws BusinessException when identity information is invalid
     *                           or the entity has already been initialized.
     */
    public void initialize(
            final String username,
            final String email,
            final String phoneNumber,
            final String registrationNumber) {

        if (this.ownerType != null || this.username != null) {
            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        if (isBlank(username)
                || isBlank(email)
                || isBlank(phoneNumber)
                || isBlank(registrationNumber)) {

            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.ownerType = RegistrationIdentityOwnerType.PENDING_REGISTRATION;
        this.registrationNumber = registrationNumber;
        this.userNumber = null;
    }

    // =========================================================================
    // Ownership Transitions
    // =========================================================================

    /**
     * Transfers identity reservation ownership from a pending registration
     * to a permanently registered user.
     *
     * <p>
     * Ownership transfer is permitted only when the reservation is
     * currently owned by the expected pending registration.
     * </p>
     *
     * <p>
     * The operation must participate in the same transaction as
     * permanent user creation and pending registration completion.
     * </p>
     *
     * @param expectedRegistrationNumber expected current registration owner.
     * @param userNumber                 permanent user business identifier.
     *
     * @throws BusinessException when ownership has already been transferred,
     *                           the expected registration does not own
     *                           the reservation, or input is invalid.
     */
    public void transferOwnershipToUser(
            final String expectedRegistrationNumber,
            final String userNumber) {

        if (isBlank(expectedRegistrationNumber) || isBlank(userNumber)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_INVALID);
        }

        if (RegistrationIdentityOwnerType.USER.equals(this.ownerType)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_IDENTITY_RESERVATION_ALREADY_TRANSFERRED);
        }

        if (!isOwnedByRegistration(expectedRegistrationNumber)) {
            throw new BusinessException(
                    AuthenticationErrorConstants.REGISTRATION_IDENTITY_RESERVATION_OWNERSHIP_MISMATCH);
        }

        this.ownerType = RegistrationIdentityOwnerType.USER;
        this.userNumber = userNumber;
        this.registrationNumber = null;
    }

    // =========================================================================
    // Ownership Verification
    // =========================================================================

    /**
     * Checks whether this reservation belongs to the specified
     * pending registration.
     *
     * @param registrationNumber registration business identifier.
     * @return true when ownership matches; false otherwise.
     */
    public boolean isOwnedByRegistration(final String registrationNumber) {

        return RegistrationIdentityOwnerType.PENDING_REGISTRATION.equals(ownerType)
                && Objects.equals(this.registrationNumber, registrationNumber);
    }

    /**
     * Checks whether this reservation belongs to the specified
     * permanent user.
     *
     * @param userNumber permanent user business identifier.
     * @return true when ownership matches; false otherwise.
     */
    public boolean isOwnedByUser(final String userNumber) {

        return RegistrationIdentityOwnerType.USER.equals(ownerType)
                && Objects.equals(this.userNumber, userNumber);
    }

    // =========================================================================
    // Internal Validation
    // =========================================================================

    /**
     * Checks whether a string is null or contains only whitespace.
     *
     * @param value value to validate.
     * @return true when the value is blank; false otherwise.
     */
    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
}
