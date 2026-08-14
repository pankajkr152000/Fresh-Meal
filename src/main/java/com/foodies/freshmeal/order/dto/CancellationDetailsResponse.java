package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CancellationDetailsResponse
 * ============================================================================
 *
 * Represents cancellation information displayed on the Admin Order Details
 * page.
 *
 * This DTO is an API representation of CancellationInfo.
 *
 * Refund information is intentionally not included here because refund
 * processing belongs to PaymentDetailsResponse.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Cancellation Status
    // =========================================================================

    /**
     * Indicates whether the order was cancelled.
     */
    private Boolean cancelled;

    /**
     * Time when the order was cancelled.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime cancelledAt;

    // =========================================================================
    // Cancellation Actor
    // =========================================================================

    /**
     * Actor who cancelled the order.
     *
     * Examples:
     *
     * CUSTOMER
     * RESTAURANT
     * DELIVERY_PARTNER
     * ADMIN
     * SYSTEM
     */
    private String cancelledBy;

    // =========================================================================
    // Cancellation Classification
    // =========================================================================

    /**
     * Source from which the cancellation originated.
     */
    private DisplayOptionResponse cancellationSource;

    /**
     * Business reason for cancellation.
     */
    private DisplayOptionResponse cancellationReason;

    // =========================================================================
    // Remarks
    // =========================================================================

    /**
     * Customer-facing cancellation remarks.
     */
    private String cancellationRemarks;

    /**
     * Internal administrative remarks.
     *
     * This field should only be exposed to authorized Admin users.
     */
    private String internalRemarks;

}