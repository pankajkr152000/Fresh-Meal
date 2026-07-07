package com.foodies.freshmeal.food.dto;

import java.util.Set;

import com.foodies.freshmeal.food.constants.FoodStatus;

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
    private String foodName;
    private String description;
    private double price;
    private String imageUrl;
    private String foodCategory;
    private String dietCategory; // veg or non-veg
    private String cuisineType;
    private String categoryGroup;
    private String foodStatus;
    private boolean isAvailable;
    private Set<FoodStatus> allowedStatuses;

}
