
package com.foodies.freshmeal.checkout.valueObject;

import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.image.dto.ImageSnapshot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Class : CheckoutItemSnapshot
 * ============================================================================
 *
 * Represents an immutable snapshot of a food item included in a checkout.
 *
 * <p>
 * The snapshot preserves the food details and pricing presented to the
 * customer during checkout review. Subsequent changes to the original food
 * entity must not modify this snapshot.
 * </p>
 *
 * <p>
 * This class is an embedded value object within CheckoutEntity and does
 * not maintain independent persistence or business lifecycle behavior.
 * </p>
 *
 * ============================================================================
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemSnapshot {

    /**
     * Unique business identifier of the food item.
     */
    private String foodNumber;

    /**
     * Food name captured at checkout time.
     */
    private String foodName;

    /**
     * Image details captured at checkout time.
     */
    private ImageSnapshot foodImage;

    /**
     * Unit price captured during checkout validation.
     */
    private Money unitPrice;

    /**
     * Quantity of the food item selected by the customer.
     */
    private int quantity;

    /**
     * Total price for this item, calculated as unit price multiplied
     * by quantity.
     */
    private Money itemTotal;
}