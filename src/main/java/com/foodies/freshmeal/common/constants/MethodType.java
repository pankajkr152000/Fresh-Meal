package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Method Types
 * ============================================================================
 *
 * Represents the generic CRUD operation performed by an API.
 *
 * This enum is technology-oriented and remains common across
 * all modules.
 *
 * Examples:
 *
 * Add Food              -> CREATE
 * View Food             -> READ
 * Update Food           -> UPDATE
 * Archive Food          -> UPDATE
 * Restore Food          -> UPDATE
 * Delete Food           -> DELETE
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum MethodType {

    /**
     * Resource creation.
     */
    CREATE,

    /**
     * Resource retrieval.
     */
    READ,

    /**
     * Resource modification.
     */
    UPDATE,

    /**
     * Resource deletion.
     */
    DELETE,
    
    /**
     * Default MethodType.
     */
    API
}