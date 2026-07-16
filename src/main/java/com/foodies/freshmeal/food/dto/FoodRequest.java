package com.foodies.freshmeal.food.dto;

import java.util.Set;

import com.foodies.freshmeal.food.constants.CategoryGroupConstant;
import com.foodies.freshmeal.food.constants.CuisineTypeConstant;
import com.foodies.freshmeal.food.constants.DietCategoryConstant;
import com.foodies.freshmeal.food.constants.FoodCategoryConstant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FoodRequest {
    private String foodName;
    private String description;
    private double price;
    private Set<FoodCategoryConstant> foodCategories;
    private DietCategoryConstant dietCategory; // veg or non-veg
    private CuisineTypeConstant cuisineType;
    private Set<CategoryGroupConstant> categoryGroup;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public DietCategoryConstant getDietCategory() {
        return dietCategory;
    }

    public void setDietCategory(DietCategoryConstant dietCategory) {
        this.dietCategory = dietCategory;
    }

}
