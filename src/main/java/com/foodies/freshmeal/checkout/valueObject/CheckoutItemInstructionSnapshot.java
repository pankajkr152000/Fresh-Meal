
package com.foodies.freshmeal.checkout.valueObject;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a snapshot of a customer's special instruction for a food item
 * included in a checkout session.
 *
 * <p>
 * The food identifier provides a stable association between the instruction
 * and its corresponding checkout item, independent of item ordering.
 * </p>
 *
 * <p>
 * The Checkout service is responsible for validating that the referenced
 * food item exists in the checkout's reviewed item snapshots.
 * </p>
 *
 * <p>
 * This value object preserves customer intent and does not contain
 * authoritative food or pricing information.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemInstructionSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique business identifier of the food item associated with
     * this instruction.
     */
    private String foodNumber;

    /**
     * Special preparation or customization instruction provided
     * by the customer.
     *
     * <p>
     * Null or blank when no special instruction was provided.
     * The Checkout service must enforce the applicable length limit.
     * </p>
     */
    private String specialInstruction;
}
