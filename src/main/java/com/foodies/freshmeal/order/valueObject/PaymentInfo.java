package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.order.constants.PaymentModeConstant;
import com.foodies.freshmeal.order.constants.PaymentStatusConstant;
import com.foodies.freshmeal.order.constants.RefundStatusConstant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * PaymentInfo
 * ============================================================================
 *
 * Represents payment and refund information associated with an order.
 *
 * Invoice information is intentionally maintained at OrderEntity level because
 * an invoice is an order/billing concern rather than a payment transaction
 * concern.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Payment mode selected by the customer.
     */
    @Builder.Default
    private PaymentModeConstant paymentMode = PaymentModeConstant.CASH_ON_DELIVERY;

    /**
     * Current payment status.
     */
    @Builder.Default
    private PaymentStatusConstant paymentStatus = PaymentStatusConstant.PENDING;

    /**
     * Payment gateway name.
     *
     * Examples:
     *
     * Razorpay
     * Stripe
     * PhonePe
     * Paytm
     */
    @Size(max = 100)
    private String paymentGateway;

    /**
     * Gateway order identifier.
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
     * Amount successfully paid.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * Currency.
     */
    @Builder.Default
    private String currency = "INR";

    /**
     * Payment completion timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime paidAt;

    /**
     * Payment failure reason.
     */
    @Size(max = 500)
    private String failureReason;

    // =========================================================================
    // Refund
    // =========================================================================

    /**
     * Current refund status.
     */
    @Builder.Default
    private RefundStatusConstant refundStatus = RefundStatusConstant.NOT_APPLICABLE;

    /**
     * Total amount refunded.
     */
    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    /**
     * Refund transaction identifier.
     */
    private String refundTransactionId;

    /**
     * Refund reason.
     */
    @Size(max = 500)
    private String refundReason;

    /**
     * Refund completion timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime refundedAt;

}