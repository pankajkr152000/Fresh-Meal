
package com.foodies.freshmeal.checkout.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a customer's request to confirm a checkout.
 *
 * <p>
 * The request identifies the checkout session and provides an idempotency
 * key to protect against duplicate confirmation attempts caused by
 * retries, network failures, or repeated client submissions.
 * </p>
 *
 * <p>
 * The authenticated user's identity must be resolved from the service
 * context. The client must not provide a user identifier.
 * </p>
 *
 * <p>
 * Pricing, item quantities, availability, and other business-critical
 * information must be retrieved and validated by the backend. This
 * request must never be treated as a source of authoritative order data.
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
public class ConfirmCheckoutRequest {

    /**
     * Unique business identifier of the checkout to be confirmed.
     */
    private String checkoutNumber;

    /**
     * Client-generated unique key identifying this confirmation attempt.
     *
     * <p>
     * The backend must persist or otherwise reliably associate this key
     * with the authenticated user and confirmation operation to provide
     * effective idempotency.
     * </p>
     */
    private String idempotencyKey;
}