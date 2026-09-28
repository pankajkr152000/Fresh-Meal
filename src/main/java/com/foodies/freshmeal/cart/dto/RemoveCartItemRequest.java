package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request DTO used to remove a food item from the authenticated customer's
 * cart.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business identifier of the food to remove.
     */
    private String foodNumber;
}