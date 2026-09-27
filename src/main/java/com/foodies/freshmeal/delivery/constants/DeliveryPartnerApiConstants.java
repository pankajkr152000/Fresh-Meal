package com.foodies.freshmeal.delivery.constants;

/**
 * ============================================================================
 * Delivery Partner API Constants
 * ============================================================================
 *
 * <p>
 * Centralized API endpoint definitions for Delivery Partner management
 * operations.
 * </p>
 *
 * <p>
 * This class contains only URI constants. It does not contain business logic,
 * authorization rules, HTTP method definitions, or service-layer behavior.
 * </p>
 *
 * <p>
 * Base URL:
 * </p>
 *
 * <pre>
 * /api/delivery-partners
 * </pre>
 *
 * <p>
 * Management operations covered by this class include:
 * </p>
 *
 * <ul>
 * <li>Retrieve Delivery Partner by ID</li>
 * <li>Retrieve Delivery Partner by user number</li>
 * <li>Update Delivery Partner profile</li>
 * <li>Update verification status</li>
 * <li>Update business status</li>
 * <li>Update availability</li>
 * </ul>
 *
 * <p>
 * Delivery Partner onboarding is intentionally exposed through a separate
 * controller and should not be mixed with management endpoints.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class DeliveryPartnerApiConstants {

    /**
     * Prevents instantiation of this constants class.
     */
    private DeliveryPartnerApiConstants() {
        throw new UnsupportedOperationException(
                "DeliveryPartnerApiConstants must not be instantiated.");
    }

    // =========================================================================
    // Base URL
    // =========================================================================

    /**
     * Base URL for Delivery Partner management APIs.
     */
    public static final String BASE_URL = "/api/delivery-partners";

    // =========================================================================
    // Read Operations
    // =========================================================================

    /**
     * Retrieves a Delivery Partner by database identifier.
     *
     * <pre>
     * POST / api / delivery - partners / view
     * </pre>
     */
    public static final String GET_BY_ID = "/view";

    /**
     * Retrieves a Delivery Partner using the associated user number.
     *
     * <pre>
     * POST / api / delivery - partners / view - by - user
     * </pre>
     */
    public static final String GET_BY_USER_NUMBER = "/view-by-user";

    // =========================================================================
    // Update Operations
    // =========================================================================

    /**
     * Updates editable Delivery Partner profile information.
     *
     * <pre>
     * PUT / api / delivery - partners / update
     * </pre>
     */
    public static final String UPDATE = "/update";

    /**
     * Updates Delivery Partner verification status.
     *
     * <pre>
     * PATCH / api / delivery - partners / verification - status
     * </pre>
     */
    public static final String UPDATE_VERIFICATION_STATUS = "/verification-status";

    /**
     * Updates Delivery Partner business status.
     *
     * <pre>
     * PATCH / api / delivery - partners / status
     * </pre>
     */
    public static final String UPDATE_STATUS = "/status";

    /**
     * Updates Delivery Partner availability.
     *
     * <pre>
     * PATCH / api / delivery - partners / availability
     * </pre>
     */
    public static final String UPDATE_AVAILABILITY = "/availability";

    /**
     * Onboard Delivery Partner.
     *
     * <pre>
     * PATCH / api / delivery - partners / onboard
     * </pre>
     */
    public static final String ONBOARD = "/onboard";
}