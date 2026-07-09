package com.foodies.freshmeal.food.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum CategoryGroupConstant implements IDisplayOption {

    MAIN_MEAL("Main Meal"),
    COURSE_TYPE("Course Type"),
    FAST_FOOD("Fast Food"),
    BEVERAGE("Beverage"),
    DESSERT("Dessert"),
    BAKERY("Bakery"),
    SNACK("Snack"),
    DRINKS("Drinks"),
    SALAD("Salad"),
    SOUP("Soup"),
    EXTRA("Extra"),
    ADD_ON("Add On"),
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
    CategoryGroupConstant(String displayName) {
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