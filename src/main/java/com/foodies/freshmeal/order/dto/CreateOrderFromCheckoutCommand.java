package com.foodies.freshmeal.order.dto;

import java.util.Objects;

import com.foodies.freshmeal.checkout.entity.CheckoutEntity;

/**
 * Internal command used to create an order from a validated checkout.
 *
 * <p>
 * This command establishes the integration boundary between the
 * checkout and order modules. It carries the validated checkout
 * information required for order creation.
 * </p>
 *
 * <p>
 * This is an internal service-layer object and must not be exposed
 * directly through REST APIs.
 * </p>
 */
public class CreateOrderFromCheckoutCommand {

    private final CheckoutEntity checkout;
    private final String idempotencyKey;

    public CreateOrderFromCheckoutCommand(
            CheckoutEntity checkout,
            String idempotencyKey) {

        this.checkout = Objects.requireNonNull(
                checkout,
                "Checkout must not be null.");

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Idempotency key must not be null or blank.");
        }

        this.idempotencyKey = idempotencyKey.trim();
    }

    public CheckoutEntity getCheckout() {
        return checkout;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}