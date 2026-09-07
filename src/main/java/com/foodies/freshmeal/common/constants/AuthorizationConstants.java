package com.foodies.freshmeal.common.constants;

public final class AuthorizationConstants {

    private AuthorizationConstants() {
    }

    /**
     * is authenticated
     */
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    /**
     * Access restricted to administrators.
     */
    public static final String ADMIN_ONLY = "hasRole('ADMIN')";

    /**
     * access restricted to User only
     */
    public static final String USER_ONLY = "hasRole('USER')";

    /**
     * Access restricted to restaurant owners.
     */
    public static final String RESTAURANT_OWNER_ONLY = "hasRole('RESTAURANT_OWNER')";

    /**
     * Access allowed to administrators and restaurant owners.
     */
    public static final String ADMIN_OR_RESTAURANT_OWNER = "hasAnyRole('ADMIN', 'RESTAURANT_OWNER')";

    /**
     * Access allowed to administrators and delivery partners.
     */
    public static final String ADMIN_OR_DELIVERY_PARTNER = "hasAnyRole('ADMIN', 'DELIVERY_PARTNER')";

    /**
     * Access allowed to administrators or the authenticated user
     * operating on their own user resource.
     */
    public static final String ADMIN_OR_SELF = "hasRole('ADMIN') or principal.userNumber == #request.userNumber";
}
