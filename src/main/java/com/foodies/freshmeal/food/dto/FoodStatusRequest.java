package com.foodies.freshmeal.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class FoodStatusRequest {
    private String foodId;

    private UpdateFoodStatusRequest updateFoodStatusRequest;
}
