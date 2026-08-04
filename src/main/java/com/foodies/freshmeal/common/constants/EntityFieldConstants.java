package com.foodies.freshmeal.common.constants;

/**
 * Centralized entity field names used for MongoDB queries.
 *
 * <p>
 * This class prevents hardcoded field names throughout the persistence layer
 * and ensures consistency across generic repositories.
 * </p>
 *
 * @author Pankaj Kumar
 */
public final class EntityFieldConstants {

    private EntityFieldConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ID = "id";

    public static final String VERSION = "version";

    public static final String DELETED_FLAG = "deletedFlag";

    public static final String RECORD_STATUS = "recordStatus";

    public static final String CREATED_AT = "createdAt";

    public static final String CREATED_BY = "createdBy";

    public static final String UPDATED_AT = "updatedAt";

    public static final String UPDATED_BY = "updatedBy";

    public static final String DELETED_AT = "deletedAt";

    public static final String DELETED_BY = "deletedBy";
}