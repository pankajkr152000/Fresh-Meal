package com.foodies.freshmeal.food.entity.impl;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;
import com.foodies.freshmeal.food.entity.IFoodEntity;

@Document(collection = "fm_food")
public class FoodEntity extends ABaseEntity implements IFoodEntity {
    private static final long serialVersionUID = 9030570160895262180L;
	@Id
    private String id;
    private String imageName;
    private String foodName;
    private String description;
    private double price;
    private FoodCategory foodCategory;
    private String imageUrl;
    private DietCategory dietCategory; // veg or non-veg
    private CuisineType cuisineType;
	private CategoryGroup categoryGroup;

	FoodEntity() {
		// Package-private constructor.
		// Entity creation should happen only through factory.
	}

	public static IEntity create() {
		return new FoodEntity();
	}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String getFoodName() {
        return foodName;
    }

    @Override
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public DietCategory getDietCategory() {
        return dietCategory;
    }

    @Override
    public void setDietCategory(DietCategory dietCategory) {
        this.dietCategory = dietCategory;
    }

    @Override
    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    @Override
    public CuisineType getCuisineType() {
        return cuisineType;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    @Override
    public void setCuisineType(CuisineType cuisineType) {
        this.cuisineType = cuisineType;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }
}
