
package com.foodies.freshmeal.checkout.validation;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.checkout.constants.CheckoutErrorConstants;
import com.foodies.freshmeal.checkout.dto.ConfirmCheckoutRequest;
import com.foodies.freshmeal.common.exception.BusinessException;

/**
 * Validates structural requirements of Checkout API requests.
 *
 * <p>
 * This validator is responsible for validating request input before
 * the Checkout service performs business operations.
 * </p>
 *
 * <p>
 * Business validations involving persisted data, authenticated user
 * ownership, checkout lifecycle status, expiry, food availability,
 * and pricing must remain in the service and domain layers.
 * </p>
 *
 * <p>
 * All validation failures are represented through centralized
 * CheckoutErrorConstants and the application's BusinessException.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class CheckoutRequestValidator {

    /**
     * Validates a checkout business identifier.
     *
     * @param checkoutNumber checkout business identifier
     * @throws BusinessException when the identifier is missing
     */
    public void validateCheckoutNumber(final String checkoutNumber) {

        if (checkoutNumber == null || checkoutNumber.isBlank()) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_NUMBER_REQUIRED);
        }
    }

    /**
     * Validates a checkout confirmation request.
     *
     * @param request confirmation request
     * @throws BusinessException when the request or its required fields
     *                           are missing
     */
    public void validateConfirmCheckoutRequest(
            final ConfirmCheckoutRequest request) {

        if (request == null) {
            throw new BusinessException(
                    CheckoutErrorConstants.INVALID_CHECKOUT_REQUEST);
        }

        validateCheckoutNumber(request.getCheckoutNumber());

        if (request.getIdempotencyKey() == null
                || request.getIdempotencyKey().isBlank()) {

            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_IDEMPOTENCY_KEY_REQUIRED);
        }
    }
}