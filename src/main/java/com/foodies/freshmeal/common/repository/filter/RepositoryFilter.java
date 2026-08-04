package com.foodies.freshmeal.common.repository.filter;

/**
 * ============================================================================
 * Repository Filter
 * ============================================================================
 *
 * Represents the visibility scope applied by the generic repository while
 * retrieving persistent documents.
 *
 * <p>
 * The repository framework automatically applies the selected filter before
 * executing MongoDB queries.
 * </p>
 *
 * <ul>
 * <li>ACTIVE - Returns only active records.</li>
 * <li>DELETED - Returns only logically deleted records.</li>
 * <li>ALL - Returns all records without applying any soft delete filter.</li>
 * </ul>
 *
 * <p>
 * This enumeration centralizes repository filtering behaviour and eliminates
 * the need for duplicated repository methods such as:
 * </p>
 *
 * <pre>
 * findByIdAndDeletedFlagFalse(...)
 * findAllByDeletedFlagFalse(...)
 * findByNameAndDeletedFlagFalse(...)
 * </pre>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum RepositoryFilter {

    /**
     * Include only active records.
     */
    ACTIVE,

    /**
     * Include only logically deleted records.
     */
    DELETED,

    /**
     * Include every record.
     */
    ALL

}