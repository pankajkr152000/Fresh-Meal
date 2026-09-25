package com.foodies.freshmeal.delivery.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Constant : Vehicle Type
 * ============================================================================
 *
 * Defines the supported vehicle types used by FreshMeal delivery partners.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum VehicleType implements IDisplayOption {

    BIKE("Bike"),
    SCOOTER("Scooter"),
    BICYCLE("Bicycle");

    private final String label;

    VehicleType(final String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return name();
    }
}