package com.foodies.freshmeal.common.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Action Types
 * ============================================================================
 *
 * Represents the business operation performed by an API.
 *
 * Unlike MethodType, ActionType describes the exact business
 * use case executed within a module.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum ActionType implements IDisplayOption {

    // =========================================================================
    // Food Operations
    // =========================================================================

    ADD_FOOD("Add Food"),
    VIEW_FOOD("View Food"),
    READ_ALL_FOODS("Read All Foods"),
    UPDATE_FOOD("Update Food"),
    UPDATE_FOOD_STATUS("Update Food Status"),
    ARCHIVE_FOOD("Archive Food"),
    RESTORE_FOOD("Restore Food"),
    READ_ARCHIVED_FOODS("Read Archived Foods"),
    PERMANENT_DELETE_FOOD("Permanent Delete Food"),

    // =========================================================================
    // Category Operations
    // =========================================================================

    ADD_CATEGORY("Add Category"),
    UPDATE_CATEGORY("Update Category"),
    DELETE_CATEGORY("Delete Category"),

    // =========================================================================
    // Image Operations
    // =========================================================================

    UPLOAD_IMAGE("Upload Image"),
    DELETE_IMAGE("Delete Image"),

    // =========================================================================
    // Order Operations
    // =========================================================================

    PLACE_ORDER("Place Order"),
    CANCEL_ORDER("Cancel Order"),

    // =========================================================================
    // Authentication
    // =========================================================================

    LOGIN("Login"),
    LOGOUT("Logout"),

    // =========================================================================
    // Restaurant Operations
    // =========================================================================

    CREATE_RESTAURANT("Create Restaurant"),
    VIEW_RESTAURANT("View Restaurant"),
    READ_ALL_RESTAURANTS("Read All Restaurants"),
    UPDATE_RESTAURANT("Update Restaurant");

    /**
     * Display name.
     */
    private final String displayName;

    ActionType(final String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getLabel() {
        return displayName;
    }

    @Override
    public String getValue() {
        return name();
    }
}