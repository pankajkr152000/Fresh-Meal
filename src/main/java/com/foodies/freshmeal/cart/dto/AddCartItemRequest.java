package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO used to add a food item to the authenticated customer's cart.
 *
 * <p>
 * Customer identity, food pricing, restaurant information, branch information,
 * food name, image, and availability are resolved by the backend and must not
 * be trusted from the client.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business identifier of the food to add.
     */
    private String foodNumber;

    /**
     * Quantity to add to the cart.
     */
    private int quantity;
}