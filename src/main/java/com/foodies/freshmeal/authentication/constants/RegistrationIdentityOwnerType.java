package com.foodies.freshmeal.authentication.constants;

/**
 * Defines the owner of a registration identity reservation.
 *
 * <p>
 * A reservation is initially owned by a pending registration.
 * After successful email verification and permanent user creation,
 * ownership is transferred to the corresponding user.
 * </p>
 */
public enum RegistrationIdentityOwnerType {

    /**
     * Reservation belongs to a pending registration.
     */
    PENDING_REGISTRATION,

    /**
     * Reservation belongs to a permanently registered user.
     */
    USER
}
