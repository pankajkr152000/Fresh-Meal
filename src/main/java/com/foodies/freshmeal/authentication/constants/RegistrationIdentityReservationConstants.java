package com.foodies.freshmeal.authentication.constants;

/**
 * ============================================================================
 * Registration Identity Reservation Constants
 * ============================================================================
 *
 * Centralized constants for the RegistrationIdentityReservationEntity.
 *
 * <p>
 * Contains collection metadata and field names used by the
 * registration identity reservation module.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class RegistrationIdentityReservationConstants {

    /**
     * MongoDB collection name.
     */
    public static final String COLLECTION_NAME = "registration_identity_reservations";

    /**
     * MongoDB field names.
     */
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PHONE_NUMBER = "phoneNumber";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_REGISTRATION_NUMBER = "registrationNumber";
    public static final String FIELD_USER_NUMBER = "userNumber";

    /**
     * Private constructor to prevent instantiation.
     */
    private RegistrationIdentityReservationConstants() {
        throw new UnsupportedOperationException(
                "RegistrationIdentityReservationConstants cannot be instantiated.");
    }
}
