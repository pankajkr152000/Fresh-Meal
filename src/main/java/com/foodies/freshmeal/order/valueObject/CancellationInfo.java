package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.order.constants.CancellationReasonConstant;
import com.foodies.freshmeal.order.constants.CancellationSourceConstant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CancellationInfo
 * ============================================================================
 *
 * Represents cancellation information for an order.
 *
 * Refund processing is intentionally handled by PaymentInfo.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether the order was cancelled.
     */
    @Builder.Default
    private Boolean cancelled = Boolean.FALSE;

    /**
     * Cancellation timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime cancelledAt;

    /**
     * Actor responsible for cancellation.
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

    /**
     * Cancellation source.
     */
    private CancellationSourceConstant cancellationSource;

    /**
     * Cancellation reason.
     */
    private CancellationReasonConstant cancellationReason;

    /**
     * Customer-facing cancellation remarks.
     */
    private String cancellationRemarks;

    /**
     * Internal administrator remarks.
     */
    private String internalRemarks;

}