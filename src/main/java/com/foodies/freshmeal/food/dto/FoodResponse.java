package com.foodies.freshmeal.food.dto;

import java.util.Set;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FoodResponse {
    private String id;
    private String foodNumber;
    private String imageName;
    private String foodName;
    private String description;
    private double price;
    private String imageUrl;
    
    private Set<DisplayOptionResponse> foodCategories;
    
    private DisplayOptionResponse dietCategory;

    private DisplayOptionResponse cuisineType;

    private Set<DisplayOptionResponse> categoryGroups;

    private DisplayOptionResponse foodStatus;
    
    private boolean isAvailable;
    private Set<DisplayOptionResponse> allowedStatuses;
    private String previousStatus;
    private String updatedAt;
    private String updatedBy;
    private String createdBy;
    private String createdAt;

}
