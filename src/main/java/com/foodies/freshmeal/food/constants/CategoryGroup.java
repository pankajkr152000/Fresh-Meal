package com.foodies.freshmeal.food.constants;

public enum CategoryGroup {

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

    private final String displayName;

    CategoryGroup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CategoryGroup fromValue(String value) {
        for (CategoryGroup category : CategoryGroup.values()) {
            if (category.name().equalsIgnoreCase(value)
                    || category.getDisplayName().equalsIgnoreCase(value)) {
                return category;
            }
        }

        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}