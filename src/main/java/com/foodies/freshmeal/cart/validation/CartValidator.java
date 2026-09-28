package com.foodies.freshmeal.cart.validation;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.cart.dto.AddCartItemRequest;
import com.foodies.freshmeal.cart.dto.RemoveCartItemRequest;
import com.foodies.freshmeal.cart.dto.UpdateCartItemRequest;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.util.CommonUtils;
import com.foodies.freshmeal.common.validation.model.ValidationError;
import com.foodies.freshmeal.common.validation.model.ValidationResult;
import com.foodies.freshmeal.common.validation.validator.AbstractValidator;

/**
 * Validator responsible for validating Cart API request objects.
 *
 * <p>
 * This validator is intentionally limited to request-level validation such as
 * required fields and basic value constraints.
 * </p>
 *
 * <p>
 * Business and persistence-related validation is handled by the Cart service
 * and domain layer. This includes cart ownership, cart existence, food
 * availability, restaurant/branch consistency, price changes, lifecycle
 * validation, and concurrency checks.
 * </p>
 */
@Component
public class CartValidator extends AbstractValidator {

    // ============================================================================
    // Add Cart Item Validation
    // ============================================================================

    /**
     * Validates an add-cart-item request.
     *
     * @param input service input containing the add-cart-item request
     */
    public void validateAddCartItem(
            final IServiceInput<AddCartItemRequest> input) {

        final AddCartItemRequest request = input.getInput();
        final ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodNumber(request.getFoodNumber(), validationResult);
            validateQuantity(request.getQuantity(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Update Cart Item Validation
    // ============================================================================

    /**
     * Validates an update-cart-item request.
     *
     * <p>
     * The supplied quantity represents the final desired quantity for the
     * cart item.
     * </p>
     *
     * @param input service input containing the update-cart-item request
     */
    public void validateUpdateCartItem(
            final IServiceInput<UpdateCartItemRequest> input) {

        final UpdateCartItemRequest request = input.getInput();
        final ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodNumber(request.getFoodNumber(), validationResult);
            validateQuantity(request.getQuantity(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Remove Cart Item Validation
    // ============================================================================

    /**
     * Validates a remove-cart-item request.
     *
     * @param input service input containing the remove-cart-item request
     */
    public void validateRemoveCartItem(
            final IServiceInput<RemoveCartItemRequest> input) {

        final RemoveCartItemRequest request = input.getInput();
        final ValidationResult validationResult = new ValidationResult();

        validateRequest(request, validationResult);

        if (request != null) {
            validateFoodNumber(request.getFoodNumber(), validationResult);
        }

        validate(validationResult);
    }

    // ============================================================================
    // Common Validation Helpers
    // ============================================================================

    /**
     * Validates a food business identifier.
     *
     * @param foodNumber       food business identifier
     * @param validationResult validation result
     */
    private void validateFoodNumber(
            final String foodNumber,
            final ValidationResult validationResult) {

        if (CommonUtils.isBlank(foodNumber)) {
            validationResult.addError(
                    ValidationError.of(
                            "foodNumber",
                            "Food Number is required."));
        }
    }

    /**
     * Validates a cart item quantity.
     *
     * <p>
     * A cart quantity must be greater than zero. The maximum quantity allowed
     * for a cart item is a business rule and should be enforced using the
     * configured Cart business rule once that limit is finalized.
     * </p>
     *
     * @param quantity         requested quantity
     * @param validationResult validation result
     */
    private void validateQuantity(
            final int quantity,
            final ValidationResult validationResult) {

        if (quantity <= 0) {
            validationResult.addError(
                    ValidationError.of(
                            "quantity",
                            "Quantity must be greater than zero."));
        }
    }

    /**
     * Validates the request object.
     *
     * @param request          request object
     * @param validationResult validation result
     */
    @Override
    protected void validateRequest(
            final Object request,
            final ValidationResult validationResult) {

        if (request == null) {
            validationResult.addError(
                    ValidationError.of(
                            "request",
                            "Request cannot be null."));
        }
    }
}