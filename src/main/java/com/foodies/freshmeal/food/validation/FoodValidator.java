package com.foodies.freshmeal.food.validation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.util.CommonUtils;
import com.foodies.freshmeal.common.validation.model.ValidationError;
import com.foodies.freshmeal.common.validation.model.ValidationResult;
import com.foodies.freshmeal.common.validation.validator.AbstractValidator;
import com.foodies.freshmeal.food.dto.ArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkArchiveFoodRequest;
import com.foodies.freshmeal.food.dto.BulkDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.BulkRestoreFoodRequest;
import com.foodies.freshmeal.food.dto.FoodRequest;
import com.foodies.freshmeal.food.dto.PermanentDeleteFoodRequest;
import com.foodies.freshmeal.food.dto.RestoreFoodRequest;

@Component
public class FoodValidator extends AbstractValidator {

    protected void doValidate(FoodRequest request) {

        if (request == null) {
            reject("request", "Request cannot be null.");
            return;
        }

        if (request.getFoodName() == null || request.getFoodName().isBlank()) {

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

    // ============================================================================
    // Archive Validation
    // ============================================================================

    /**
     * Validates archive food request.
     *
     * @param input Archive food request.
     */
    public void validateArchiveFood(IServiceInput<ArchiveFoodRequest> input) {

        ArchiveFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodId(request.getFoodId(), validationResult);
        }

        validate(validationResult);
    }

    /**
     * Validates bulk archive food request.
     *
     * @param input Bulk archive request.
     */
    public void validateBulkArchiveFoods(IServiceInput<BulkArchiveFoodRequest> input) {

        BulkArchiveFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodIds(request.getFoodIds(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Restore Validation
    // ============================================================================

    /**
     * Validates restore food request.
     *
     * @param input Restore food request.
     */
    public void validateRestoreFood(IServiceInput<RestoreFoodRequest> input) {

        RestoreFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodId(request.getFoodId(), validationResult);
        }

        validate(validationResult);
    }

    /**
     * Validates bulk restore food request.
     *
     * @param input Bulk restore request.
     */
    public void validateBulkRestoreFoods(IServiceInput<BulkRestoreFoodRequest> input) {

        BulkRestoreFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodIds(request.getFoodIds(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Permanent Delete Validation
    // ============================================================================

    /**
     * Validates permanent delete food request.
     *
     * @param input Permanent delete request.
     */
    public void validatePermanentDeleteFood(IServiceInput<PermanentDeleteFoodRequest> input) {

        PermanentDeleteFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodId(request.getFoodId(), validationResult);
        }

        validate(validationResult);
    }

    /**
     * Validates bulk permanent delete request.
     *
     * @param input Bulk permanent delete request.
     */
    public void validateBulkPermanentDeleteFoods(IServiceInput<BulkDeleteFoodRequest> input) {

        BulkDeleteFoodRequest request = input.getInput();

        ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodIds(request.getFoodIds(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Common Validation Helpers
    // ============================================================================

    /**
     * Validates a single food identifier.
     *
     * @param foodId           Food identifier.
     * @param validationResult Validation result.
     */
    private void validateFoodId(final String foodId, final ValidationResult validationResult) {

        if (CommonUtils.isBlank(foodId)) {

            validationResult.addError(ValidationError.of("foodId", "Food Id is required."));
        }
    }

    /**
     * Validates multiple food identifiers.
     *
     * @param foodIds          Food identifiers.
     * @param validationResult Validation result.
     */
    private void validateFoodIds(final List<String> foodIds, final ValidationResult validationResult) {

        if (foodIds == null || foodIds.isEmpty()) {

            validationResult.addError(ValidationError.of("foodIds", "At least one Food Id is required."));
        }
    }

    /**
     * Validates request object.
     *
     * @param request          Request object.
     * @param validationResult Validation result.
     */
    @Override
    protected void validateRequest(final Object request, final ValidationResult validationResult) {

        if (request == null) {

            validationResult.addError(ValidationError.of("request", "Request cannot be null."));
        }
    }

}