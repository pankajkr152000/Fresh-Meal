package com.foodies.freshmeal.food.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum FoodCategoryConstant implements IDisplayOption {

    // Main Meals
    BREAKFAST("Breakfast", CategoryGroupConstant.MAIN_MEAL),
    LUNCH("Lunch", CategoryGroupConstant.MAIN_MEAL),
    DINNER("Dinner", CategoryGroupConstant.MAIN_MEAL),
    BIRYANI("Biryani", CategoryGroupConstant.MAIN_MEAL),

    // Course Types
    STARTER("Starter", CategoryGroupConstant.COURSE_TYPE),
    MAIN_COURSE("Main Course", CategoryGroupConstant.COURSE_TYPE),
    SIDE_DISH("Side Dish", CategoryGroupConstant.COURSE_TYPE),

    // Fast Food
    PIZZA("Pizza", CategoryGroupConstant.FAST_FOOD),
    BURGER("Burger", CategoryGroupConstant.FAST_FOOD),
    SANDWICH("Sandwich", CategoryGroupConstant.FAST_FOOD),
    Roll("Roll", CategoryGroupConstant.FAST_FOOD),

    // Desserts
    DESSERT("Dessert", CategoryGroupConstant.DESSERT),
    ICE_CREAM("Ice Cream", CategoryGroupConstant.DESSERT),

    // Bakery
    BREAD("Bread", CategoryGroupConstant.BAKERY),
    CAKE("Cake", CategoryGroupConstant.BAKERY),

    // Drinks and Beverages
    HOT_BEVERAGE("Hot Beverage", CategoryGroupConstant.BEVERAGE),
    COLD_BEVERAGE("Cold Beverage", CategoryGroupConstant.BEVERAGE),
    JUICE("Juice", CategoryGroupConstant.DRINKS),
    MOCKTAIL("Mocktail", CategoryGroupConstant.DRINKS),
    SMOOTHIE("Smoothie", CategoryGroupConstant.DRINKS),

    // Salads & Soups
    SALAD("Salad", CategoryGroupConstant.SALAD),
    SOUP("Soup", CategoryGroupConstant.SOUP),

    // Snacks
    SNACK("Snack", CategoryGroupConstant.SNACK),
    FINGER_FOOD("Finger Food", CategoryGroupConstant.SNACK),

    // Other
    OTHER("Other", CategoryGroupConstant.OTHER);

    private final String displayName;
    private final CategoryGroupConstant group;

    FoodCategoryConstant(String displayName, CategoryGroupConstant group) {
        this.displayName = displayName;
        this.group = group;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CategoryGroupConstant getGroup() {
        return group;
    }

    /**
     * ============================================================================
     * Returns the user-friendly label displayed in the UI.
     *
     * @return display label
     *         ============================================================================
     */
    @Override
    public String getLabel() {
        return this.displayName;
    }

    /**
     * ============================================================================
     * Returns the internal enum value used by the application.
     *
     * <p>
     * This value is used for:
     * <ul>
     * <li>API communication</li>
     * <li>Business logic</li>
     * <li>Persistence</li>
     * <li>Filtering</li>
     * </ul>
     * </p>
     *
     * <p>
     * Example:
     * 
     * <pre>
     * AVAILABLE      -> "AVAILABLE"
     * OUT_OF_STOCK   -> "OUT_OF_STOCK"
     * </pre>
     * </p>
     *
     * @return enum constant name
     *         ============================================================================
     */
    @Override
    public String getValue() {
        return this.name();
    }
}