package com.foodies.freshmeal.food.dto;

import java.time.LocalDateTime;

import com.foodies.freshmeal.food.constants.FoodStatusConstant;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Response after successful food status update.
 */
@Getter
@Setter
@Builder
@Data
public class FoodStatusResponse {

    private String foodId;

    private FoodStatusConstant previousStatus;

    private FoodStatusConstant foodStatus;

    private LocalDateTime updatedAt;

    private String updatedBy;

}
