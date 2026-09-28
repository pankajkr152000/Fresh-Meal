package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO used to update the quantity of an existing cart item.
 *
 * <p>
 * The supplied quantity represents the final desired quantity, not the
 * quantity to increment.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business identifier of the food whose quantity is being updated.
     */
    private String foodNumber;

    /**
     * Final desired quantity.
     */
    private int quantity;
}