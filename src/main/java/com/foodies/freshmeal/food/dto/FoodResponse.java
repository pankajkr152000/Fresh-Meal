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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodResponse {
    private String id;
    private String imageName;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private FoodCategory foodCategory; 
	private DietCategory dietCategory; // veg or non-veg
	private CuisineType cuisineType;
	private CategoryGroup categoryGroup;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public DietCategory getDietCategory() {
		return dietCategory;
	}
	public void setDietCategory(DietCategory dietCategory) {
		this.dietCategory = dietCategory;
	}
    
}
