package com.foodies.freshmeal.food.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.food.constants.CategoryGroup;
import com.foodies.freshmeal.food.constants.CuisineType;
import com.foodies.freshmeal.food.constants.DietCategory;
import com.foodies.freshmeal.food.constants.FoodCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "fm_food")
public class FoodEntity extends ABaseEntity {
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

   
}
