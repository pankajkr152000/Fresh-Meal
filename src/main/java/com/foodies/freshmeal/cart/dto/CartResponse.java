package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing the customer's cart.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cartNumber;

    private String restaurantNumber;

    private String restaurantBranchNumber;

    private CartStatusConstant status;

    private LocalDateTime expiresAt;

    private List<CartItemResponse> items;

    private Money subtotal;

    /**
     * Number of distinct food lines in the cart.
     */
    private int totalItemCount;

    /**
     * Total quantity across all cart items.
     */
    private int totalQuantity;
}