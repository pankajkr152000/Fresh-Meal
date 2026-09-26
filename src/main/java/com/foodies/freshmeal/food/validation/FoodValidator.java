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

	// ============================================================================
	// Food Request Validation
	// ============================================================================

	/**
	 * Validates the food creation/update request.
	 *
	 * <p>
	 * This validation is intentionally limited to validating the contents of the
	 * food request. Restaurant ownership, authorization, restaurant resolution,
	 * status transitions, and persistence-related validation belong to the
	 * service/business layer.
	 *
	 * @param request Food request.
	 */
	protected void doValidate(final FoodRequest request) {

		if (request == null) {
			reject("request", "Request cannot be null.");
			return;
		}

		if (CommonUtils.isBlank(request.getFoodName())) {
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
	public void validateArchiveFood(final IServiceInput<ArchiveFoodRequest> input) {

		final ArchiveFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	public void validateBulkArchiveFoods(final IServiceInput<BulkArchiveFoodRequest> input) {

		final BulkArchiveFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	public void validateRestoreFood(final IServiceInput<RestoreFoodRequest> input) {

		final RestoreFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	public void validateBulkRestoreFoods(final IServiceInput<BulkRestoreFoodRequest> input) {

		final BulkRestoreFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	public void validatePermanentDeleteFood(final IServiceInput<PermanentDeleteFoodRequest> input) {

		final PermanentDeleteFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	public void validateBulkPermanentDeleteFoods(final IServiceInput<BulkDeleteFoodRequest> input) {

		final BulkDeleteFoodRequest request = input.getInput();
		final ValidationResult validationResult = new ValidationResult();

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
	 * <p>
	 * Each identifier is validated individually to prevent blank identifiers from
	 * being accepted inside an otherwise non-empty collection.
	 *
	 * @param foodIds          Food identifiers.
	 * @param validationResult Validation result.
	 */
	private void validateFoodIds(final List<String> foodIds, final ValidationResult validationResult) {

		if (foodIds == null || foodIds.isEmpty()) {
			validationResult.addError(ValidationError.of("foodIds", "At least one Food Id is required."));
			return;
		}

		for (int index = 0; index < foodIds.size(); index++) {

			if (CommonUtils.isBlank(foodIds.get(index))) {
				validationResult.addError(ValidationError.of("foodIds[" + index + "]", "Food Id cannot be blank."));
			}
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