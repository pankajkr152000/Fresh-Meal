package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.order.constants.CancellationReasonConstant;
import com.foodies.freshmeal.order.constants.CancellationSourceConstant;

import jakarta.validation.Valid;
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
 * Represents cancellation details of an order.
 *
 * This value object captures who cancelled the order, why it was cancelled,
 * refund information, and any additional remarks.
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
public class CancellationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether the order is cancelled.
     */
    @Builder.Default
    private Boolean cancelled = Boolean.FALSE;

    /**
     * Cancellation timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime cancelledAt;

    /**
     * User who cancelled the order.
     *
     * Example:
     * Customer
     * Restaurant
     * Delivery Partner
     * Admin
     * System
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
     * Customer/Admin remarks.
     */
    private String cancellationRemarks;

    /**
     * Internal remarks.
     *
     * Visible only to administrators.
     */
    private String internalRemarks;

    /**
     * Indicates whether refund is applicable.
     */
    @Builder.Default
    private Boolean refundRequired = Boolean.FALSE;

    /**
     * Refund amount.
     */
    @Valid
    private Money refundAmount;

    /**
     * Indicates whether refund has been completed.
     */
    @Builder.Default
    private Boolean refunded = Boolean.FALSE;

    /**
     * Refund completed timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime refundedAt;

}