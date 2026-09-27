package com.foodies.freshmeal.delivery.validation;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.delivery.constants.DeliveryPartnerErrorConstants;
import com.foodies.freshmeal.delivery.constants.VehicleType;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerAvailabilityRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerStatusRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerVerificationRequest;

/**
 * ============================================================================
 * Validator : Delivery Partner
 * ============================================================================
 *
 * <p>
 * Provides request-level and domain-input validation for Delivery Partner
 * operations.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Validate Delivery Partner service input objects.</li>
 * <li>Validate required business identifiers.</li>
 * <li>Validate required request payloads.</li>
 * <li>Validate request values that are not fully covered by Bean
 * Validation.</li>
 * </ul>
 *
 * <h3>Design Boundaries</h3>
 *
 * <p>
 * This validator intentionally does not:
 * </p>
 *
 * <ul>
 * <li>Access repositories.</li>
 * <li>Perform database lookups.</li>
 * <li>Perform authorization checks.</li>
 * <li>Generate identifiers.</li>
 * <li>Perform persistence operations.</li>
 * <li>Perform Delivery Partner status transitions.</li>
 * <li>Map DTOs to entities.</li>
 * </ul>
 *
 * <p>
 * Database-dependent and state-dependent business rules remain the
 * responsibility of {@code DeliveryPartnerServiceImpl}.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class DeliveryPartnerValidator {

    // =========================================================================
    // Registration Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner onboarding request.
     *
     * <p>
     * Bean Validation annotations on
     * {@link DeliveryPartnerRegistrationRequest} handle basic field-level
     * constraints. This method additionally ensures that the complete request
     * object is present.
     * </p>
     *
     * @param input Delivery Partner registration service input
     *
     * @throws NullPointerException if the service input or request is null
     */
    public void validateRegistration(
            final IServiceInput<DeliveryPartnerRegistrationRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner registration service input must not be null.");

        final DeliveryPartnerRegistrationRequest request = Objects.requireNonNull(
                input.getInput(),
                DeliveryPartnerErrorConstants.DELIVERY_PARTNER_REGISTRATION_REQUIRED
                        .getErrorMessage());

        validateVehicleNumber(request.getVehicleNumber());
        validateVehicleType(request.getVehicleType());
    }

    // =========================================================================
    // Update Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner profile update request.
     *
     * @param input Delivery Partner update service input
     *
     * @throws NullPointerException if the service input or request is null
     */
    public void validateUpdate(
            final IServiceInput<DeliveryPartnerUpdateRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner update service input must not be null.");

        final DeliveryPartnerUpdateRequest request = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner update request must not be null.");

        validatePartnerNumber(request.getPartnerNumber());
        validateVehicleNumber(request.getVehicleNumber());
        validateVehicleType(request.getVehicleType());
    }

    // =========================================================================
    // Verification Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner verification-status update request.
     *
     * @param input Delivery Partner verification service input
     *
     * @throws NullPointerException if the service input or request is null
     */
    public void validateVerificationStatus(
            final IServiceInput<DeliveryPartnerVerificationRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner verification service input must not be null.");

        final DeliveryPartnerVerificationRequest request = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner verification request must not be null.");

        validatePartnerNumber(request.getPartnerNumber());

        Objects.requireNonNull(
                request.getVerificationStatus(),
                DeliveryPartnerErrorConstants.VERIFICATION_STATUS_REQUIRED
                        .getErrorMessage());
    }

    // =========================================================================
    // Status Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner lifecycle-status update request.
     *
     * @param input Delivery Partner status service input
     *
     * @throws NullPointerException if the service input or request is null
     */
    public void validateStatus(
            final IServiceInput<DeliveryPartnerStatusRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner status service input must not be null.");

        final DeliveryPartnerStatusRequest request = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner status request must not be null.");

        validatePartnerNumber(request.getPartnerNumber());

        Objects.requireNonNull(
                request.getStatus(),
                DeliveryPartnerErrorConstants.STATUS_REQUIRED
                        .getErrorMessage());
    }

    // =========================================================================
    // Availability Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner availability update request.
     *
     * @param input Delivery Partner availability service input
     *
     * @throws NullPointerException if the service input or request is null
     */
    public void validateAvailability(
            final IServiceInput<DeliveryPartnerAvailabilityRequest> input) {

        Objects.requireNonNull(
                input,
                "Delivery Partner availability service input must not be null.");

        final DeliveryPartnerAvailabilityRequest request = Objects.requireNonNull(
                input.getInput(),
                "Delivery Partner availability request must not be null.");

        validatePartnerNumber(request.getPartnerNumber());

        Objects.requireNonNull(
                request.getAvailable(),
                DeliveryPartnerErrorConstants.AVAILABILITY_REQUIRED
                        .getErrorMessage());
    }

    // =========================================================================
    // Common Validation
    // =========================================================================

    /**
     * Validates a Delivery Partner business identifier.
     *
     * @param partnerNumber Delivery Partner number
     *
     * @throws IllegalArgumentException if the partner number is blank
     */
    private void validatePartnerNumber(final String partnerNumber) {

        if (partnerNumber == null || partnerNumber.isBlank()) {

            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.DELIVERY_PARTNER_ID_REQUIRED
                            .getErrorMessage());
        }
    }

    /**
     * Validates the vehicle number.
     *
     * @param vehicleNumber vehicle registration number
     *
     * @throws IllegalArgumentException if the vehicle number is blank
     */
    private void validateVehicleNumber(final String vehicleNumber) {

        if (vehicleNumber == null || vehicleNumber.isBlank()) {

            throw new IllegalArgumentException(
                    DeliveryPartnerErrorConstants.VEHICLE_NUMBER_REQUIRED
                            .getErrorMessage());
        }
    }

    /**
     * Validates the vehicle type.
     *
     * @param vehicleType vehicle type
     *
     * @throws NullPointerException if the vehicle type is null
     */
    private void validateVehicleType(final VehicleType vehicleType) {

        Objects.requireNonNull(
                vehicleType,
                DeliveryPartnerErrorConstants.VEHICLE_TYPE_REQUIRED
                        .getErrorMessage());
    }
}