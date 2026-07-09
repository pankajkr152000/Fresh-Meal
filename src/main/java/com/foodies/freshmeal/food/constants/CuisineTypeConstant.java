package com.foodies.freshmeal.food.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum CuisineTypeConstant implements IDisplayOption {

    // Cuisines
    INDIAN("Indian"),
    CHINESE("Chinese"),
    ITALIAN("Italian"),
    MEXICAN("Mexican"),
    THAI("Thai"),
    JAPANESE("Japanese"),
    MEDITERRANEAN("Mediterranean"),
    AMERICAN("American"),
    KOREAN("Korean"),
    CONTINENTAL("Continental"),
    OTHER("Other");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    /**
     * Creates a delivery status.
     *
     * @param displayName user-friendly display label
     */
    CuisineTypeConstant(String displayName) {
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
