package com.foodies.freshmeal.user.constants;

/**
 * ============================================================================
 * Address API Constants
 * ============================================================================
 *
 * Centralized API endpoint constants for the Address module.
 *
 * ============================================================================
 */
public final class AddressApiConstants {

    private AddressApiConstants() {
        // Utility class.
    }

    /**
     * Add a new address.
     */
    public static final String ADD = "/add";

    /**
     * Get address by business address number.
     */
    public static final String GET_BY_ID = "/get";
}