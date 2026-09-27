package com.foodies.freshmeal.delivery.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerAvailabilityRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerStatusRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerUpdateRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerVerificationRequest;

/**
 * ============================================================================
 * Service : Delivery Partner
 * ============================================================================
 *
 * <p>
 * Defines business operations for delivery-partner profile management,
 * verification, business status, and operational availability.
 * </p>
 *
 * <p>
 * Delivery-partner onboarding is performed through the registration operation.
 * A user may register for the delivery-partner role, but the resulting
 * delivery-partner profile remains pending until it is reviewed by an
 * authorized administrator.
 * </p>
 *
 * <p>
 * User identity and authentication remain responsibilities of the User and
 * Authentication domains. This service manages only delivery-partner-specific
 * business responsibilities.
 * </p>
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 * <li>Delivery-partner onboarding</li>
 * <li>Delivery-partner retrieval</li>
 * <li>Delivery-partner profile update</li>
 * <li>Administrative verification</li>
 * <li>Business status management</li>
 * <li>Operational availability management</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IDeliveryPartnerService {

    // =========================================================================
    // Registration / Onboarding
    // =========================================================================

    /**
     * Registers a new delivery-partner profile for the current user.
     *
     * <p>
     * A newly registered delivery partner starts with pending verification,
     * pending business status, and unavailable operational state.
     * </p>
     *
     * @param input delivery-partner registration input
     * @return registered delivery-partner details
     */
    // IServiceOutput<DeliveryPartnerResponse> register(
    // IServiceInput<DeliveryPartnerRegistrationRequest> input);

    // =========================================================================
    // Retrieval
    // =========================================================================

    /**
     * Retrieves a delivery-partner profile by its database identifier.
     *
     * @param input delivery-partner identifier input
     * @return delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> getById(
            IServiceInput<String> input);

    /**
     * Retrieves a delivery-partner profile by the associated FreshMeal user
     * number.
     *
     * @param input user number input
     * @return delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> getByUserNumber(
            IServiceInput<String> input);

    // =========================================================================
    // Profile Update
    // =========================================================================

    /**
     * Updates editable delivery-partner profile information.
     *
     * @param input delivery-partner update input
     * @return updated delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> update(
            IServiceInput<DeliveryPartnerUpdateRequest> input);

    // =========================================================================
    // Verification
    // =========================================================================

    /**
     * Updates the verification status of a delivery partner.
     *
     * <p>
     * This operation represents an administrative verification decision.
     * Authorization must therefore be enforced by the application/security
     * layer so that a delivery partner cannot verify their own profile.
     * </p>
     *
     * @param input delivery-partner verification input
     * @return updated delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> updateVerificationStatus(
            IServiceInput<DeliveryPartnerVerificationRequest> input);

    // =========================================================================
    // Business Status
    // =========================================================================

    /**
     * Updates the business lifecycle status of a delivery partner.
     *
     * @param input delivery-partner status update input
     * @return updated delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> updateStatus(
            IServiceInput<DeliveryPartnerStatusRequest> input);

    // =========================================================================
    // Operational Availability
    // =========================================================================

    /**
     * Updates the operational availability of a delivery partner.
     *
     * @param input delivery-partner availability update input
     * @return updated delivery-partner details
     */
    IServiceOutput<DeliveryPartnerResponse> updateAvailability(
            IServiceInput<DeliveryPartnerAvailabilityRequest> input);
}