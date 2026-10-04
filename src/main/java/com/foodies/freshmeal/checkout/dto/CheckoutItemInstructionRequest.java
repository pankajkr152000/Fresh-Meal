
package com.foodies.freshmeal.checkout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * Checkout Item Instruction Request
 * ============================================================================
 *
 * Represents a customer's special instruction for an individual food item
 * during checkout review.
 *
 * <p>
 * The food number associates the instruction with an item in the checkout.
 * The service layer must verify that the referenced food belongs to the
 * current checkout before accepting the instruction.
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
public class CheckoutItemInstructionRequest {

    /**
     * Stable identifier of the food item to which the instruction applies.
     */
    @NotBlank
    private String foodNumber;

    /**
     * Optional customer instruction for preparing the food item.
     */
    @Size(max = 300)
    private String specialInstruction;
}
