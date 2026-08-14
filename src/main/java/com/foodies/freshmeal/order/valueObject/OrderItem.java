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
 * This is a historical snapshot of the food at the time the order was placed.
 *
 * Pricing values are calculated and controlled by the backend.
 *
 * ============================================================================
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
     */
    private String dietCategory;

    /**
     * Quantity ordered.
     */
    @NotNull
    @Min(1)
    private Integer quantity;

    /**
     * Unit price at the time of order.
     */
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal unitPrice;

    /**
     * Discount applied to this item.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Tax applied to this item.
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
     * Examples:
     *
     * Less spicy
     * Extra cheese
     * No onion
     */
    @Size(max = 500)
    private String specialInstruction;

}