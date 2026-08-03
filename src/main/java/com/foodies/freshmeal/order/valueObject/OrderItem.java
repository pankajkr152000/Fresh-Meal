package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderItem
 * ============================================================================
 *
 * Represents a single food item ordered by the customer.
 *
 * This class is a Value Object that stores a snapshot of the food at the time
 * the order was placed. It intentionally duplicates selected food information
 * to preserve historical accuracy.
 *
 * Example:
 *
 * Veg Burger
 * Quantity : 2
 * Unit Price : ₹120
 * Line Total : ₹240
 *
 * Even if the food price later changes to ₹150, this order will still display
 * ₹120 because it stores the original snapshot.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Food unique identifier.
     */
    @NotBlank
    private String foodId;

    /**
     * Food name snapshot.
     */
    @NotBlank
    @Size(max = 150)
    private String foodName;

    /**
     * Food description snapshot.
     */
    @Size(max = 500)
    private String description;

    /**
     * Food image snapshot.
     */
    private String imageUrl;

    /**
     * Food category snapshot.
     */
    private String foodCategory;

    /**
     * Cuisine snapshot.
     */
    private String cuisineType;

    /**
     * Diet category snapshot.
     *
     * Veg
     * Non-Veg
     * Vegan
     * Jain
     */
    private String dietCategory;

    /**
     * Quantity ordered.
     */
    @NotNull
    @Min(1)
    private Integer quantity;

    /**
     * Price of one unit at the time of ordering.
     */
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal unitPrice;

    /**
     * Discount applied on this item.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Tax applied on this item.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * Final line total.
     *
     * Formula:
     *
     * (Unit Price × Quantity)
     * - Discount
     * + Tax
     */
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal lineTotal;

    /**
     * Customer-specific instruction.
     *
     * Example:
     * - Less spicy
     * - Extra cheese
     * - No onion
     */
    @Size(max = 500)
    private String specialInstruction;

    /**
     * Whether this item is currently available.
     *
     * Useful when viewing historical orders if the food has since been
     * discontinued.
     */
    @Builder.Default
    private Boolean available = Boolean.TRUE;
}