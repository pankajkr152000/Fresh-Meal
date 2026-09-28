package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.image.dto.ImageSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing an individual item in a customer's cart.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String foodNumber;

    private String foodName;

    private ImageSnapshot foodImage;

    private Money unitPrice;

    private int quantity;

    private Money itemTotal;

    private LocalDateTime addedAt;
}