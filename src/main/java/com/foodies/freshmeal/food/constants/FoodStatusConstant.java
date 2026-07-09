package com.foodies.freshmeal.food.constants;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Enum : FoodStatus
 * ============================================================================
 *
 * Represents the lifecycle status of a food item.
 *
 * A food can exist in only one status at any given time.
 *
 * These statuses control whether a food is visible to customers,
 * available for ordering, or retained only for historical purposes.
 *
 * Business Meaning
 * ----------------
 *
 * AVAILABLE
 * Food can be ordered.
 *
 * OUT_OF_STOCK
 * Food exists but currently cannot be ordered.
 *
 * DISABLED
 * Food is temporarily disabled by an administrator.
 *
 * COMING_SOON
 * Food has been created but is not yet launched.
 *
 * SEASONAL
 * Food is available only during specific seasons.
 *
 * DISCONTINUED
 * Food has been permanently removed from sale.
 *
 * ============================================================================
 *
 * Allowed Status Transitions
 *
 * AVAILABLE
 * -> OUT_OF_STOCK
 * -> DISABLED
 * -> DISCONTINUED
 *
 * OUT_OF_STOCK
 * -> AVAILABLE
 * -> DISABLED
 *
 * DISABLED
 * -> AVAILABLE
 *
 * COMING_SOON
 * -> AVAILABLE
 *
 * SEASONAL
 * -> AVAILABLE
 *
 * DISCONTINUED
 * -> (No transitions allowed)
 *
 * ============================================================================
 */

public enum FoodStatusConstant implements IDisplayOption {

    AVAILABLE("Available"),

    OUT_OF_STOCK("Out of Stock"),

    DISABLED("Disabled"),

    COMING_SOON("Coming Soon"),

    SEASONAL("Seasonal"),

    DISCONTINUED("Discontinued"),

    Deprecated("Deprecated");

    /**
     * User-friendly display name of the food status.
     * <p>
     * This value is intended for API responses and UI rendering,
     * avoiding the need for clients to format enum names.
     * </p>
     */
    private final String displayName;

    /**
     * Creates a food status with its corresponding display name.
     *
     * @param displayName human-readable status name
     */
    FoodStatusConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns all valid next statuses for the current status.
     */
    public Set<FoodStatusConstant> getAllowedTransitions() {

        return switch (this) {

            case AVAILABLE -> EnumSet.of(
                    OUT_OF_STOCK,
                    DISABLED,
                    DISCONTINUED);

            case OUT_OF_STOCK -> EnumSet.of(
                    AVAILABLE,
                    DISABLED);

            case DISABLED -> EnumSet.of(
                    AVAILABLE);

            case COMING_SOON -> EnumSet.of(
                    AVAILABLE);

            case SEASONAL -> EnumSet.of(
                    AVAILABLE);

            case DISCONTINUED -> EnumSet.of(
                    COMING_SOON);

            case Deprecated -> EnumSet.noneOf(FoodStatusConstant.class);

        };

    }

    /**
     * Returns true if transition is allowed.
     */
    public boolean canTransitionTo(FoodStatusConstant newStatus) {

        return getAllowedTransitions().contains(newStatus);

    }

    /**
     * Returns all valid next statuses for the current status.
     *
     * <p>
     * The returned values are user-friendly display names intended
     * for API responses and frontend rendering.
     * </p>
     *
     * @return immutable set of allowed transition display names
     */
    public Set<String> getAllowedTransitionsString() {

        return getAllowedTransitions()
                .stream()
                .map(status -> status.getLabel())
                .collect(Collectors.toUnmodifiableSet());

    }

    public Set<DisplayOptionResponse> getAllowedTransitionOptions() {

        return getAllowedTransitions()
                .stream()
                .map(status -> new DisplayOptionResponse(
                        status.getLabel(),
                        status.name()))
                .collect(Collectors.toUnmodifiableSet());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return name();
    }

}
