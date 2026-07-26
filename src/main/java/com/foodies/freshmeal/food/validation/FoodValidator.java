package com.foodies.freshmeal.food.validation;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.validation.validator.AbstractValidator;
import com.foodies.freshmeal.food.dto.FoodRequest;

@Component
public class FoodValidator extends AbstractValidator<FoodRequest> {

    @Override
    protected void doValidate(FoodRequest request) {

        if (request == null) {
            reject("request", "Request cannot be null.");
            return;
        }

        if (request.getFoodName() == null
                || request.getFoodName().isBlank()) {

            reject("foodName", "Food name is required.");
        }

        if (request.getPrice() <= 0) {

            reject("price", "Price must be greater than zero.");
        }

        if (request.getDietCategory() == null) {

            reject("dietCategory", "Diet category is required.");
        }

        if (request.getCuisineType() == null) {

            reject("cuisineType", "Cuisine type is required.");
        }
    }
}