package com.foodies.freshmeal.food.constants;

public enum CuisineType {

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

    private final String displayName;

    CuisineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CuisineType fromValue(String value) {
        for (CuisineType category : CuisineType.values()) {
            if (category.name().equalsIgnoreCase(value)
                    || category.getDisplayName().equalsIgnoreCase(value)) {
                return category;
            }
        }

        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}
