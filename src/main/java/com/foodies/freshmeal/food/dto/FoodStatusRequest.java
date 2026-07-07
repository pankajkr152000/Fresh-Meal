package com.foodies.freshmeal.food.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodStatusRequest {
    private String foodId;

    private UpdateFoodStatusRequest updateFoodStatusRequest;
}
