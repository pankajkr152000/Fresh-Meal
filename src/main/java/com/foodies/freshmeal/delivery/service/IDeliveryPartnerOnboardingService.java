package com.foodies.freshmeal.delivery.service;

import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerRegistrationRequest;
import com.foodies.freshmeal.delivery.dto.DeliveryPartnerResponse;

/**
 * ============================================================================
 * Service : Delivery Partner Onboarding
 * ============================================================================
 *
 * <p>
 * Defines the onboarding workflow for users registering as delivery partners.
 * </p>
 *
 * <p>
 * Delivery-partner onboarding is intentionally separated from the delivery
 * partner management service. This service establishes the delivery-partner
 * business profile for the currently authenticated user, while management
 * operations remain responsible for an already established profile.
 * </p>
 *
 * <p>
 * The authenticated user identity is obtained from the request-scoped service
 * context. Client-supplied user identity information is not trusted.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IDeliveryPartnerOnboardingService {

    /**
     * Onboards the currently authenticated user as a delivery partner.
     *
     * <p>
     * A newly created delivery-partner profile starts in pending verification,
     * pending business status, and unavailable operational state.
     * </p>
     *
     * @param input delivery-partner onboarding input
     * @return newly created delivery-partner profile
     */
    IServiceOutput<DeliveryPartnerResponse> onboardDeliveryPartner(
            IServiceInput<DeliveryPartnerRegistrationRequest> input);
}