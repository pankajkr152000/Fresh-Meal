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
 * OrderListResponse
 * ============================================================================
 *
 * Represents a single order record displayed in the Admin Order List.
 *
 * This DTO intentionally contains only the information required by the
 * Order Management table.
 *
 * Detailed information such as:
 *
 * - Delivery address
 * - Order items
 * - Payment gateway details
 * - Refund details
 * - Delivery partner
 * - Timeline
 * - Cancellation information
 * - Metadata
 *
 * should be loaded through OrderDetailsResponse.
 *
 * ============================================================================
 *
 * Design Principle
 * ----------------
 *
 * OrderEntity is the persistence model.
 *
 * OrderListResponse is the API projection required by the Admin Order List.
 *
 * The API should never expose OrderEntity directly.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Order Identification
    // =========================================================================

    /**
     * MongoDB document identifier.
     */
    private String id;

    /**
     * Business-facing order number.
     *
     * Example:
     *
     * FM202608140001
     */
    private String orderNumber;

    // =========================================================================
    // Customer
    // =========================================================================

    /**
     * Lightweight customer information required by the order table.
     */
    private CustomerSummary customer;

    // =========================================================================
    // Restaurant
    // =========================================================================

    /**
     * Lightweight restaurant information required by the order table.
     */
    private RestaurantSummary restaurant;

    // =========================================================================
    // Order Classification
    // =========================================================================

    /**
     * Order fulfillment type.
     *
     * Example:
     *
     * DELIVERY
     * TAKEAWAY
     * DINE_IN
     */
    private DisplayOptionResponse orderType;

    /**
     * Current business status of the order.
     */
    private DisplayOptionResponse orderStatus;

    /**
     * Current delivery status.
     *
     * May be null for order types that do not require delivery.
     */
    private DisplayOptionResponse deliveryStatus;

    /**
     * Current payment status.
     */
    private DisplayOptionResponse paymentStatus;

    // =========================================================================
    // Order Quantity
    // =========================================================================

    /**
     * Number of distinct food items in the order.
     */
    private Integer totalItems;

    /**
     * Total quantity across all ordered items.
     */
    private Integer totalQuantity;

    // =========================================================================
    // Pricing
    // =========================================================================

    /**
     * Final payable amount.
     *
     * Amount and currency are represented together through Money.
     */
    private Money grandTotal;

    // =========================================================================
    // Timeline
    // =========================================================================

    /**
     * Time at which the order was placed.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime orderedAt;

    // =========================================================================
    // Nested Summary DTOs
    // =========================================================================

    /**
     * Lightweight customer projection for the Order List.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerSummary implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Customer identifier.
         */
        private String customerId;

        /**
         * Customer display name.
         */
        private String customerName;
    }

    /**
     * Lightweight restaurant projection for the Order List.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestaurantSummary implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Restaurant identifier.
         */
        private String restaurantId;

        /**
         * Restaurant display name.
         */
        private String restaurantName;
    }
}