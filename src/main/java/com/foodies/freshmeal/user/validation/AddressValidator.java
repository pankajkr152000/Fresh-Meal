package com.foodies.freshmeal.user.validation;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.user.constants.AddressErrorConstants;
import com.foodies.freshmeal.user.dto.AddressNumberRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;

/**
 * ============================================================================
 * Validator : AddressValidator
 * ============================================================================
 *
 * Provides business-level validation for customer address operations.
 *
 * <p>
 * Bean validation annotations such as {@code @NotBlank}, {@code @NotNull},
 * {@code @Size} and {@code @Pattern} remain responsible for request-field
 * validation at the API boundary. This validator is responsible only for
 * validations that require explicit business rules.
 * </p>
 *
 * <p>
 * Persistence-related validations such as address existence and ownership
 * should remain in the service layer because they require repository access.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class AddressValidator {

    /**
     * Validates an address creation request.
     *
     * @param request address creation request
     */
    public void validateCreateRequest(final AddressRequest request) {

        if (request == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_REQUEST_REQUIRED);
        }
    }

    /**
     * Validates an address update request.
     *
     * @param request address update request
     */
    public void validateUpdateRequest(final AddressUpdateRequest request) {

        if (request == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_REQUEST_REQUIRED);
        }

        if (request.getAddressNumber() == null
                || request.getAddressNumber().isBlank()) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_NUMBER_REQUIRED);
        }
    }

    /**
     * Validates a business address-number request.
     *
     * @param request address number request
     */
    public void validateAddressNumberRequest(
            final AddressNumberRequest request) {

        if (request == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_NUMBER_REQUIRED);
        }

        if (request.getAddressNumber() == null
                || request.getAddressNumber().isBlank()) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_NUMBER_REQUIRED);
        }
    }
}