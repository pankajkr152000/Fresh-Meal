package com.foodies.freshmeal.food.constants;

import java.util.EnumSet;
import java.util.Set;

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

public enum FoodStatus {

    AVAILABLE,

    OUT_OF_STOCK,

    DISABLED,

    COMING_SOON,

    SEASONAL,

    DISCONTINUED,

    Deprecated;

    /**
     * Returns all valid next statuses for the current status.
     */
    public Set<FoodStatus> getAllowedTransitions() {

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

            case Deprecated -> EnumSet.noneOf(FoodStatus.class);

        };

    }

    /**
     * Returns true if transition is allowed.
     */
    public boolean canTransitionTo(FoodStatus newStatus) {

        return getAllowedTransitions().contains(newStatus);

    }

}
