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
 * OrderStatusUpdateRequest
 * ============================================================================
 *
 * Represents a request to change the current business status of an order.
 *
 * This DTO is intentionally limited to information required to perform a
 * status transition.
 *
 * The actual transition rules are enforced by the Order business/service layer.
 *
 * ============================================================================
 *
 * Example
 * -------
 *
 * {
 * "status": "CONFIRMED",
 * "reason": "Restaurant accepted the order",
 * "remarks": "Kitchen notified"
 * }
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Status
    // =========================================================================

    /**
     * Target order status.
     *
     * Example:
     *
     * CONFIRMED
     * PREPARING
     * READY_FOR_PICKUP
     * CANCELLED
     */
    @NotBlank(message = "Order status is required.")
    private String status;

    // =========================================================================
    // Reason
    // =========================================================================

    /**
     * Business reason associated with the status change.
     *
     * This is particularly useful for exceptional transitions such as
     * cancellation or rejection.
     */
    @Size(max = 500, message = "Reason cannot exceed 500 characters.")
    private String reason;

    // =========================================================================
    // Remarks
    // =========================================================================

    /**
     * Additional administrative remarks associated with the status change.
     */
    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters.")
    private String remarks;

}