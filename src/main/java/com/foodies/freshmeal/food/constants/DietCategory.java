package com.foodies.freshmeal.food.constants;

public enum DietCategory {

    VEG("Veg"),
    NON_VEG("Non Veg"),
    VEGAN("Vegan"),
    JAIN("Jain"),
    EGGETARIAN("Eggetarian");

    private final String displayName;

    DietCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DietCategory fromValue(String value) {
        for (DietCategory category : DietCategory.values()) {
            if (category.name().equalsIgnoreCase(value)
                    || category.getDisplayName().equalsIgnoreCase(value)) {
                return category;
            }
        }

        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}