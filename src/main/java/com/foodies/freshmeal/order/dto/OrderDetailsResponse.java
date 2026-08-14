package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
 * OrderDetailsResponse
 * ============================================================================
 *
 * Represents the complete order information required by the Admin Order
 * Details page.
 *
 * This DTO is intentionally separated from OrderEntity so that the MongoDB
 * persistence model and REST API contract remain independent.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponse implements Serializable {

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
     */
    private DisplayOptionResponse deliveryStatus;

    // =========================================================================
    // Customer
    // =========================================================================

    /**
     * Customer information associated with the order.
     */
    private CustomerDetailsResponse customer;

    // =========================================================================
    // Restaurant
    // =========================================================================

    /**
     * Restaurant information associated with the order.
     */
    private RestaurantDetailsResponse restaurant;

    // =========================================================================
    // Delivery Address
    // =========================================================================

    /**
     * Delivery address captured for this order.
     */
    private AddressDetailsResponse deliveryAddress;

    // =========================================================================
    // Ordered Items
    // =========================================================================

    /**
     * Food items included in the order.
     */
    private List<OrderItemDetailsResponse> items;

    // =========================================================================
    // Pricing
    // =========================================================================

    /**
     * Complete pricing breakdown.
     */
    private PriceDetailsResponse priceSummary;

    // =========================================================================
    // Payment
    // =========================================================================

    /**
     * Payment and refund information.
     */
    private PaymentDetailsResponse payment;

    // =========================================================================
    // Delivery
    // =========================================================================

    /**
     * Delivery partner assigned to the order.
     *
     * Null when no partner has been assigned.
     */
    private DeliveryPartnerDetailsResponse deliveryPartner;

    // =========================================================================
    // Timeline
    // =========================================================================

    /**
     * Complete order lifecycle timeline.
     */
    private TimelineDetailsResponse timeline;

    // =========================================================================
    // Cancellation
    // =========================================================================

    /**
     * Cancellation information.
     *
     * Null when the order has not been cancelled.
     */
    private CancellationDetailsResponse cancellation;

    // =========================================================================
    // Notes
    // =========================================================================

    /**
     * Order-level notes.
     */
    private OrderNotesResponse notes;

    // =========================================================================
    // Metadata
    // =========================================================================

    /**
     * Technical and marketing metadata.
     */
    private OrderMetadataResponse metadata;

    // =========================================================================
    // Order Statistics
    // =========================================================================

    /**
     * Number of distinct food items.
     */
    private Integer totalItems;

    /**
     * Total quantity across all ordered items.
     */
    private Integer totalQuantity;

    // =========================================================================
    // Scheduled Order
    // =========================================================================

    /**
     * Indicates whether this is a scheduled order.
     */
    private Boolean scheduledOrder;

    /**
     * Scheduled delivery date and time.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime scheduledDeliveryAt;

    // =========================================================================
    // Gift Order
    // =========================================================================

    /**
     * Indicates whether this is a gift order.
     */
    private Boolean giftOrder;

    // =========================================================================
    // Invoice
    // =========================================================================

    /**
     * Indicates whether an invoice has been generated.
     */
    private Boolean invoiceGenerated;

    /**
     * Invoice number.
     */
    private String invoiceNumber;

    // =========================================================================
    // Tags
    // =========================================================================

    /**
     * Internal order tags.
     */
    private List<String> tags;

    // =========================================================================
    // Audit Information
    // =========================================================================

    /**
     * Record creation timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    /**
     * Last modification timestamp.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

}