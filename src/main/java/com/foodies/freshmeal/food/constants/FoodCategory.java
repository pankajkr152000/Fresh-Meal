package com.foodies.freshmeal.food.constants;

public enum FoodCategory {

    // Main Meals
    BREAKFAST("Breakfast", CategoryGroup.MAIN_MEAL),
    LUNCH("Lunch", CategoryGroup.MAIN_MEAL),
    DINNER("Dinner", CategoryGroup.MAIN_MEAL),

    // Course Types
    STARTER("Starter", CategoryGroup.COURSE_TYPE),
    MAIN_COURSE("Main Course", CategoryGroup.COURSE_TYPE),
    SIDE_DISH("Side Dish", CategoryGroup.COURSE_TYPE),

    // Fast Food
    PIZZA("Pizza", CategoryGroup.FAST_FOOD),
    BURGER("Burger", CategoryGroup.FAST_FOOD),
    SANDWICH("Sandwich", CategoryGroup.FAST_FOOD),

    // Desserts
    DESSERT("Dessert", CategoryGroup.DESSERT),
    ICE_CREAM("Ice Cream", CategoryGroup.DESSERT),

    // Bakery
    BREAD("Bread", CategoryGroup.BAKERY),
    CAKE("Cake", CategoryGroup.BAKERY),

    // Drinks and Beverages
    HOT_BEVERAGE("Hot Beverage", CategoryGroup.BEVERAGE),
    COLD_BEVERAGE("Cold Beverage", CategoryGroup.BEVERAGE),
    JUICE("Juice", CategoryGroup.DRINKS),
    MOCKTAIL("Mocktail", CategoryGroup.DRINKS),
    SMOOTHIE("Smoothie", CategoryGroup.DRINKS),

    // Salads & Soups
    SALAD("Salad", CategoryGroup.SALAD),
    SOUP("Soup", CategoryGroup.SOUP),

    // Snacks
    SNACK("Snack", CategoryGroup.SNACK),
    FINGER_FOOD("Finger Food", CategoryGroup.SNACK),

    // Other
    OTHER("Other", CategoryGroup.OTHER);

    private final String displayName;
    private final CategoryGroup group;

    FoodCategory(String displayName, CategoryGroup group) {
        this.displayName = displayName;
        this.group = group;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CategoryGroup getGroup() {
        return group;
    }

    public static FoodCategory fromValue(String value) {
        for (FoodCategory category : FoodCategory.values()) {
            if (category.name().equalsIgnoreCase(value)
                    || category.getDisplayName().equalsIgnoreCase(value)) {
                return category;
            }
        }

        throw new IllegalArgumentException("No enum constant with value " + value);
    }
}