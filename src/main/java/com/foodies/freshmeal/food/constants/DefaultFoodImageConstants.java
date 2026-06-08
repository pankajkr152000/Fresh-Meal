package com.foodies.freshmeal.food.constants;

/**
 * Default system images used throughout the application.
 *
 * Purpose:
 * - Avoid broken image links.
 * - Provide fallback images.
 * - Ensure consistent UI experience.
 *
 * Future Enhancements:
 * - Add category-specific placeholders.
 * - Add tenant-specific branding.
 */
public final class DefaultFoodImageConstants {


    /**
     * Default food url.
     */
    //public static final String DEFAULT_FOOD_IMAGE_URL = "https://freshmeal-images.s3.ap-south-1.amazonaws.com/default/food-placeholder.png"; 

    /**
     * Default food image.
     */
    public static final String DEFAULT_FOOD_IMAGE = "food-placeholder.png"; 
    public static final String DEFAULT_FOOD_IMAGE_URL = "https://freshmeal-images.s3.ap-south-1.amazonaws.com/default/food-placeholder.png"; 
    /**
     * Default user profile image.
     */
    // USER("user-placeholder.png"),

    /**
     * Default restaurant image.
     */
    public static final String DEFAULT_RESTAURANT_IMAGE = "restaurant-placeholder.png"; 
    public static final String DEFAULT_FOOD_RESTAURANT_URL = "https://freshmeal-images.s3.ap-south-1.amazonaws.com/default/food-placeholder.png"; 
    /**
     * Default category image.
     */
    public static final String DEFAULT_CATEGORY_IMAGE = "category-placeholder.png";
    public static final String DEFAULT_CATEGORY_IMAGE_URL = "https://freshmeal-images.s3.ap-south-1.amazonaws.com/default/food-placeholder.png"; 
    /**
     * Default offer/banner image.
     */
    public static final String DEFAULT_OFFER_IMAGE = "offer-placeholder.png"; 
    public static final String DEFAULT_OFFER_IMAGE_URL = "https://freshmeal-images.s3.ap-south-1.amazonaws.com/default/food-placeholder.png"; 

    private DefaultFoodImageConstants() {
        
    }

}
