package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.valueObject.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * PaymentDetailsResponse
 * ============================================================================
 *
 * Represents payment and refund information displayed on the Admin Order
 * Details page.
 *
 * This DTO is an API representation of PaymentInfo and intentionally keeps
 * the persistence/domain model separate from the REST API contract.
 *
 * Monetary values use the common Money value object so that amount and
 * currency remain together.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Payment
    // =========================================================================

    /**
     * Payment mode selected by the customer.
     */
    private DisplayOptionResponse paymentMode;

    /**
     * Current payment status.
     */
    private DisplayOptionResponse paymentStatus;

    /**
     * Payment gateway used for the transaction.
     *
     * Example:
     *
     * Razorpay
     * PhonePe
     * Paytm
     */
    private String paymentGateway;

    /**
     * Payment gateway order identifier.
     */
    private String gatewayOrderId;

    /**
     * Application transaction identifier.
     */
    private String transactionId;

    /**
     * Gateway payment identifier.
     */
    private String gatewayPaymentId;

    /**
     * Gateway reference identifier.
     */
    private String gatewayReferenceId;

    /**
     * Payment authorization identifier.
     */
    private String authorizationId;

    /**
     * Amount successfully paid by the customer.
     */
    private Money paidAmount;

    /**
     * Payment completion timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime paidAt;

    /**
     * Payment failure reason, when applicable.
     */
    private String failureReason;

    // =========================================================================
    // Refund
    // =========================================================================

    /**
     * Current refund status.
     */
    private DisplayOptionResponse refundStatus;

    /**
     * Total amount refunded.
     */
    private Money refundedAmount;

    /**
     * Refund transaction identifier.
     */
    private String refundTransactionId;

    /**
     * Refund reason.
     */
    private String refundReason;

    /**
     * Refund completion timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime refundedAt;

}