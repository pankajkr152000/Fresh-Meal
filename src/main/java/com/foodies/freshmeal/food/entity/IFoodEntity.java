package com.foodies.freshmeal.food.entity;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;

public interface IFoodEntity extends IEntity {
    String getId();
    String getImageName();
    String getFoodName();
    String getDescription();
    double getPrice();
    FoodCategory getFoodCategory();
    String getImageUrl();
    DietCategory getDietCategory();
    CuisineType getCuisineType();
    CategoryGroup getCategoryGroup();
    

    public void setId(String name);
    public void setImageName(String imageName);
    public void setFoodName(String foodName);
    public void setDescription(String description);
    public void setPrice(double price);
    public void setFoodCategory(FoodCategory category);
    public void setImageUrl(String imageUrl);
    public void setDietCategory(DietCategory dietCategory);
    public void setCuisineType(CuisineType cuisineType); 
    public void setCategoryGroup(CategoryGroup categoryGroup); 


}
