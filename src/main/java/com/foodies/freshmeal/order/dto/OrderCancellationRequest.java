package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderCancellationRequest
 * ============================================================================
 *
 * Represents a request to cancel an existing order.
 *
 * This DTO contains only information that needs to be supplied by the caller.
 *
 * The following information is intentionally NOT accepted from the client:
 *
 * - cancelledBy
 * - cancellationSource
 * - cancelledAt
 * - refundRequired
 * - refundAmount
 * - refunded
 * - refundedAt
 *
 * These values are determined by the backend based on the authenticated actor,
 * order state, cancellation policy, payment information, and business rules.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancellationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Cancellation Reason
    // =========================================================================

    /**
     * Business reason for cancelling the order.
     *
     * The value should correspond to one of the supported cancellation
     * reasons defined by CancellationReasonConstant.
     *
     * Example:
     *
     * CUSTOMER_REQUEST
     * RESTAURANT_UNAVAILABLE
     * OUT_OF_STOCK
     */
    @NotBlank(message = "Cancellation reason is required.")
    private String cancellationReason;

    // =========================================================================
    // Remarks
    // =========================================================================

    /**
     * Additional remarks explaining the cancellation.
     */
    @Size(max = 500, message = "Cancellation remarks cannot exceed 500 characters.")
    private String cancellationRemarks;

}