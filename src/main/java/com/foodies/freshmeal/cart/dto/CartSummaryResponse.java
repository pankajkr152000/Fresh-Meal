package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;

import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Lightweight response DTO containing the customer's cart summary.
 *
 * <p>
 * This DTO is intended for lightweight UI use cases where the complete
 * cart-item collection is not required.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cartNumber;

    private CartStatusConstant status;

    private int totalItemCount;

    private int totalQuantity;

    private Money subtotal;
}