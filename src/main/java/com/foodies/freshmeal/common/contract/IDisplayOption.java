package com.foodies.freshmeal.common.contract;

/**
 * ============================================================================
 * Interface : IDisplayOption
 * ============================================================================
 *
 * Represents a reusable UI display option consisting of a human-readable
 * label and its corresponding internal value.
 *
 * <p>
 * This interface is intended to be implemented by enums whose values
 * are exposed to the frontend as selectable options such as:
 * </p>
 *
 * <ul>
 *   <li>Food Status</li>
 *   <li>Food Category</li>
 *   <li>Diet Category</li>
 *   <li>Cuisine Type</li>
 *   <li>Restaurant Status</li>
 *   <li>Order Status</li>
 * </ul>
 *
 * <p>
 * Implementing this interface enables generic conversion of enums into
 * UI-friendly option objects without writing duplicate mapping logic.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 * ============================================================================
 */
public interface IDisplayOption {

    /**
     * Returns the user-friendly label displayed in the UI.
     *
     * @return display label
     */
    String getLabel();

    /**
     * Returns the internal value used for business logic and API communication.
     *
     * @return internal value
     */
    String getValue();
}
