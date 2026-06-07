package com.foodies.freshmeal.food.dto;

import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;

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
    private FoodCategory foodCategory; 
	private DietCategory dietCategory; // veg or non-veg
	private CuisineType cuisineType;
	private CategoryGroup categoryGroup;
    
    
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
	public DietCategory getDietCategory() {
		return dietCategory;
	}
	public void setDietCategory(DietCategory dietCategory) {
		this.dietCategory = dietCategory;
	}
	
    
}
