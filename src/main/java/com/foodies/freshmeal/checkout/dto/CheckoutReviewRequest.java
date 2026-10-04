
package com.foodies.freshmeal.checkout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * Checkout Review Request
 * ============================================================================
 *
 * Represents a customer's request to prepare a checkout session for review.
 *
 * <p>
 * The request identifies the checkout session and carries the customer's
 * intended order preferences. The Checkout service is responsible for
 * validating these preferences against the current checkout, cart, food,
 * restaurant, and pricing data.
 * </p>
 *
 * <p>
 * Client-supplied values are treated as untrusted input. The backend
 * independently calculates and validates all authoritative pricing data.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutReviewRequest {

    /**
     * Business identifier of the checkout session to review.
     */
    @NotBlank
    private String checkoutNumber;

    /**
     * Customer-selected order preferences.
     */
    @Valid
    @NotNull
    private CheckoutOrderPreferencesRequest orderPreferences;
}
