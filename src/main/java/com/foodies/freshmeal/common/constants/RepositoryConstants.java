package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Repository Constants
 * ============================================================================
 *
 * Centralized constants used by the generic repository framework.
 *
 * <p>
 * This class contains repository-specific constants that should never be
 * hardcoded throughout the persistence layer.
 * </p>
 *
 * <p>
 * Examples:
 * </p>
 *
 * <ul>
 * <li>Soft delete flags</li>
 * <li>System user</li>
 * <li>Default persistence values</li>
 * </ul>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class RepositoryConstants {

    /**
     * Prevent instantiation.
     */
    private RepositoryConstants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Represents an active (non-deleted) entity.
     */
    public static final Boolean ACTIVE = Boolean.FALSE;

    /**
     * Represents a logically deleted entity.
     */
    public static final Boolean DELETED = Boolean.TRUE;

    /**
     * Default system user.
     */
    public static final String SYSTEM_USER = "SYSTEM";

}