package com.foodies.freshmeal.food.constants;

/**
 * Food API endpoint constants.
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class FoodApiConstants {

    private FoodApiConstants() {
    }

    // =========================================================
    // Base URL
    // =========================================================

    public static final String BASE_URL = "/api/foods";

    // =========================================================
    // Food CRUD APIs
    // =========================================================

    public static final String ADD = "/add";
    public static final String UPDATE_FOOD_STATUS = "/{foodId}/status";
    public static final String DELETE = "/delete";
    //public static final String GET_FOOD_BY_FOOD_ID = "api/foods/view/{foodId}";
    public static final String GET_FOOD_BY_FOOD_ID = "/view";
    public static final String READ_ALL_FOODS = "/readAllFoods";
    public static final String EDIT_FOOD = "/edit";

    // =========================================================
    // Food Search APIs
    // =========================================================

    public static final String SEARCH = "/search";
    public static final String SEARCH_BY_ID = "/search/id";
    public static final String SEARCH_BY_NAME = "/search/name";
    public static final String SEARCH_BY_CATEGORY = "/search/category";
    public static final String SEARCH_BY_CUISINE = "/search/cuisine";
    public static final String SEARCH_BY_DIET = "/search/diet";
    public static final String SEARCH_BY_GROUP = "/search/group";

    // =========================================================
    // Food Metadata APIs
    // =========================================================

    public static final String FOOD_CATEGORIES = "/metadata/food-categories";
    public static final String DIET_CATEGORIES = "/metadata/diet-categories";
    public static final String CUISINE_CATEGORIES = "/metadata/cuisine-categories";
    public static final String GROUP_CATEGORIES = "/metadata/group-categories";
    public static final String FOOD_CATEGORY_METADATA = "/foodCategoryMetadata";

    // =========================================================
    // Food Image APIs
    // =========================================================

    public static final String UPLOAD_IMAGE = "/image/upload";
    public static final String UPDATE_IMAGE = "/image/update";
    public static final String DELETE_IMAGE = "/image/delete";
    public static final String GET_IMAGE = "/image/{imageId}";

    // =========================================================
    // Food Status APIs
    // =========================================================

    public static final String ACTIVATE = "/activate/{foodId}";
    public static final String DEACTIVATE = "/deactivate/{foodId}";
    public static final String TOGGLE_STATUS = "/toggle-status/{foodId}";

    // =========================================================
    // Food Filter APIs
    // =========================================================

    public static final String FILTER = "/filter";
    public static final String FILTER_BY_PRICE = "/filter/price";
    public static final String FILTER_BY_CATEGORY = "/filter/category";
    public static final String FILTER_BY_CUISINE = "/filter/cuisine";
    public static final String FILTER_BY_DIET = "/filter/diet";

    // =========================================================
    // Food Pagination APIs
    // =========================================================

    public static final String PAGE = "/page";

    // =========================================================
    // Food Analytics APIs
    // =========================================================

    public static final String COUNT = "/count";
    public static final String STATISTICS = "/statistics";
    public static final String TOP_SELLING = "/top-selling";
    public static final String TRENDING = "/trending";
}