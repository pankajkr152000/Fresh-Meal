package com.foodies.freshmeal.user.constants;

/**
 * ============================================================================
 * Constants : AddressApiConstants
 * ============================================================================
 *
 * Defines API endpoint mappings for address management operations.
 *
 * <p>
 * This class contains only URI-related constants and does not contain any
 * business or authorization logic.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class AddressApiConstants {

    /**
     * Base URI for address APIs.
     */
    public static final String BASE_URL = "/api/addresses";

    /**
     * Creates a new address.
     */
    public static final String ADD = "/add";

    /**
     * Retrieves an address using its business-facing address number.
     */
    public static final String GET_BY_ID = "/view";

    /**
     * Retrieves all active addresses belonging to the authenticated user.
     */
    public static final String GET_MY_ADDRESSES = "/my-addresses";

    /**
     * Updates an existing address.
     */
    public static final String UPDATE = "/update";

    /**
     * Sets an address as the default address.
     */
    public static final String SET_DEFAULT = "/set-default";

    /**
     * Soft deletes an address.
     */
    public static final String DELETE = "/delete";

    /**
     * Prevents instantiation.
     */
    private AddressApiConstants() {
        throw new UnsupportedOperationException(
                "AddressApiConstants must not be instantiated.");
    }
}