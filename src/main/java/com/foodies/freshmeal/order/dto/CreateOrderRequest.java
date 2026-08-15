package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;
import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.order.constants.OrderTypeConstant;
import com.foodies.freshmeal.order.constants.PaymentModeConstant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CreateOrderRequest
 * ============================================================================
 *
 * Represents the client request used to place a new FreshMeal order.
 *
 * <p>
 * This DTO intentionally contains only information that the client is allowed
 * to request during order placement.
 * </p>
 *
 * <p>
 * The request must NOT contain trusted historical/order values such as:
 * </p>
 *
 * <ul>
 * <li>Customer identity</li>
 * <li>Customer snapshot</li>
 * <li>Restaurant snapshot</li>
 * <li>Food name or food price</li>
 * <li>Order number</li>
 * <li>Order status</li>
 * <li>Delivery status</li>
 * <li>Payment status</li>
 * <li>Grand total</li>
 * <li>Tax calculation</li>
 * <li>Discount calculation</li>
 * </ul>
 *
 * <p>
 * Those values are resolved and calculated by the backend during order
 * creation.
 * </p>
 *
 * ============================================================================
 *
 * Request Flow
 * ------------
 *
 * Client
 * |
 * v
 * CreateOrderRequest
 * |
 * v
 * IOrderService
 * |
 * v
 * OrderServiceImpl
 * |
 * +---- IServiceContext -> authenticated customer
 * |
 * +---- Food/Restaurant/Address validation
 * |
 * +---- Pricing calculation
 * |
 * +---- Historical snapshots
 * |
 * v
 * OrderEntity
 *
 * ============================================================================
 *
 * Security Principle
 * ------------------
 *
 * The authenticated customer is obtained from IServiceContext.
 *
 * A client must never be trusted to provide another customer's identity
 * through this request.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Order Classification
    // =========================================================================

    /**
     * Defines how the order will be fulfilled.
     *
     * Examples:
     *
     * DELIVERY
     * TAKEAWAY
     * DINE_IN
     */
    @NotNull
    private OrderTypeConstant orderType;

    // =========================================================================
    // Restaurant
    // =========================================================================

    /**
     * Identifier of the restaurant from which the order is being placed.
     *
     * <p>
     * The service layer will load the restaurant and create the historical
     * RestaurantSnapshot stored inside the OrderEntity.
     * </p>
     */
    @NotBlank
    private String restaurantId;

    // =========================================================================
    // Ordered Items
    // =========================================================================

    /**
     * Food items requested by the customer.
     *
     * <p>
     * Food name, price, tax and discount information are intentionally not
     * accepted from the client. They are resolved from the current Food
     * master data and calculated by the backend.
     * </p>
     */
    @NotEmpty
    @Valid
    @Size(max = 100)
    private List<OrderItemRequest> items;

    // =========================================================================
    // Delivery Address
    // =========================================================================

    /**
     * Customer address identifier to be used for delivery.
     *
     * <p>
     * Required when orderType is DELIVERY.
     *
     * For TAKEAWAY or DINE_IN orders this may be null.
     * </p>
     *
     * <p>
     * The service layer loads the customer's address and creates an immutable
     * AddressSnapshot inside the OrderEntity.
     * </p>
     */
    private String deliveryAddressId;

    // =========================================================================
    // Payment
    // =========================================================================

    /**
     * Payment method selected by the customer.
     *
     * <p>
     * This represents the requested payment method only. The resulting
     * payment status is controlled by the payment/order workflow.
     * </p>
     */
    @NotNull
    private PaymentModeConstant paymentMode;

    // =========================================================================
    // Coupon
    // =========================================================================

    /**
     * Coupon code supplied by the customer.
     *
     * <p>
     * The backend must validate the coupon before applying any discount.
     * </p>
     */
    @Size(max = 50)
    private String couponCode;

    // =========================================================================
    // Tip
    // =========================================================================

    /**
     * Optional tip requested by the customer.
     *
     * <p>
     * The amount is validated and incorporated into the server-side pricing
     * calculation. The client must never provide the final grand total.
     * </p>
     */
    @Valid
    private Money tipAmount;

    // =========================================================================
    // Scheduled Order
    // =========================================================================

    /**
     * Indicates whether the order should be scheduled for a future time.
     */
    @Builder.Default
    private Boolean scheduledOrder = Boolean.FALSE;

    /**
     * Requested scheduled delivery time.
     *
     * <p>
     * Required when scheduledOrder is true and ignored/rejected otherwise.
     * The service layer performs the cross-field validation.
     * </p>
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime scheduledDeliveryAt;

    // =========================================================================
    // Gift Order
    // =========================================================================

    /**
     * Indicates whether this order is being placed as a gift.
     */
    @Builder.Default
    private Boolean giftOrder = Boolean.FALSE;

    // =========================================================================
    // Notes
    // =========================================================================

    /**
     * Customer-facing order note.
     *
     * <p>
     * Restaurant and internal notes are not accepted from the customer.
     * </p>
     */
    @Size(max = 500)
    private String customerNote;

    // =========================================================================
    // Nested Item Request
    // =========================================================================

    /**
     * Represents one food item requested during order placement.
     *
     * <p>
     * This is intentionally a command/request object rather than the domain
     * OrderItem value object.
     * </p>
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Food identifier.
         */
        @NotBlank
        private String foodId;

        /**
         * Quantity requested by the customer.
         */
        @NotNull
        @Min(1)
        @Max(999)
        private Integer quantity;

        /**
         * Optional customer instruction for this item.
         *
         * Examples:
         *
         * Less spicy
         * No onion
         * Extra cheese
         */
        @Size(max = 300)
        private String specialInstruction;
    }

}