package com.foodies.freshmeal.food.dto;

import java.time.LocalDateTime;

import com.foodies.freshmeal.food.constants.FoodStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response after successful food status update.
 */
@Getter
@Setter
@Builder
public class FoodStatusResponse {

    private String foodId;

    private FoodStatus previousStatus;

    private FoodStatus currentStatus;

    private LocalDateTime updatedAt;

    private String updatedBy;

}
