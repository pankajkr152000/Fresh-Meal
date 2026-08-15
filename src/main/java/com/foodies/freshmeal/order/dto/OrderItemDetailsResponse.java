package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderItemDetailsResponse
 * ============================================================================
 *
 * Represents a single food item displayed on the Admin Order Details page.
 *
 * The response contains the historical food snapshot and item-level pricing
 * captured when the order was placed.
 *
 * This DTO is intentionally independent from the OrderItem domain Value Object.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Food Identification
    // =========================================================================

    /**
     * Food unique identifier.
     */
    private String foodId;

    /**
     * Food name captured at the time of ordering.
     */
    private String foodName;

    /**
     * Food description captured at the time of ordering.
     */
    private String description;

    /**
     * Food image URL captured at the time of ordering.
     */
    private String imageUrl;

    // =========================================================================
    // Food Classification
    // =========================================================================

    /**
     * Food category.
     */
    private String foodCategory;

    /**
     * Cuisine type.
     */
    private String cuisineType;

    /**
     * Diet category.
     */
    private String dietCategory;

    // =========================================================================
    // Quantity
    // =========================================================================

    /**
     * Quantity ordered.
     */
    private Integer quantity;

    // =========================================================================
    // Pricing
    // =========================================================================

    /**
     * Unit price at the time the order was placed.
     */
    private Money unitPrice;

    /**
     * Discount applied to this item.
     */
    private Money discountAmount;

    /**
     * Tax applied to this item.
     */
    private Money taxAmount;

    /**
     * Final amount for this order line.
     *
     * Formula:
     *
     * (Unit Price × Quantity)
     * - Discount
     * + Tax
     */
    private Money lineTotal;

    // =========================================================================
    // Customer Instruction
    // =========================================================================

    /**
     * Special instruction provided for this food item.
     *
     * Examples:
     *
     * Less spicy
     * No onion
     * Extra cheese
     */
    private String specialInstruction;

}