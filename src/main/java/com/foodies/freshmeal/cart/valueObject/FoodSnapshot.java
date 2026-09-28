package com.foodies.freshmeal.cart.valueObject;

import java.io.Serializable;

import com.foodies.freshmeal.image.dto.ImageSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Immutable snapshot of food information captured when the food is added
 * to the cart.
 *
 * <p>
 * The snapshot prevents the cart from depending on the current state of
 * {@code FoodEntity}. Food information may change after the item has been
 * added to the cart, but the cart should retain the information that was
 * originally captured.
 * </p>
 *
 * <p>
 * Pricing is intentionally not part of this value object. The cart item
 * maintains its own unit-price snapshot because price has independent
 * business rules and must be revalidated during checkout.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business identifier of the food.
     */
    private String foodNumber;

    /**
     * Food name captured at the time the item was added to the cart.
     */
    private String foodName;

    /**
     * Food image information captured at the time the item was added
     * to the cart.
     */
    private ImageSnapshot foodImage;
}