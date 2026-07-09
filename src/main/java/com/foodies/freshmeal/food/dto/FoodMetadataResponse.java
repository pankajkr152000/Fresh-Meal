package com.foodies.freshmeal.food.dto;

import java.util.Set;

import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodMetadataResponse {

    private Set<DisplayOptionResponse> foodCategories;

    private Set<DisplayOptionResponse> dietCategories;

    private Set<DisplayOptionResponse> cuisineCategories;

    private Set<DisplayOptionResponse> groupCategories;

    private Set<DisplayOptionResponse> foodStatuses;

}
