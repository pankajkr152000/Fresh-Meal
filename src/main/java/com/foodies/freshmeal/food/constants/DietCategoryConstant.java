package com.foodies.freshmeal.food.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum DietCategoryConstant implements IDisplayOption {

    VEG("Veg"),
    NON_VEG("Non Veg"),
    VEGAN("Vegan"),
    JAIN("Jain"),
    EGGETARIAN("Eggetarian");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a delivery status.
     *
     * @param displayName user-friendly display label
     */
    DietCategoryConstant(String displayName) {
        this.displayName = displayName;
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