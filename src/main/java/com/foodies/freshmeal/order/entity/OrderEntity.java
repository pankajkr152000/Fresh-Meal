package com.foodies.freshmeal.order.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.order.constants.DeliveryStatusConstant;
import com.foodies.freshmeal.order.constants.OrderStatusConstant;
import com.foodies.freshmeal.order.constants.OrderTypeConstant;
import com.foodies.freshmeal.order.valueObject.CancellationInfo;
import com.foodies.freshmeal.order.valueObject.CustomerSnapshot;
import com.foodies.freshmeal.order.valueObject.DeliveryPartnerSnapshot;
import com.foodies.freshmeal.order.valueObject.OrderItem;
import com.foodies.freshmeal.order.valueObject.OrderMetadata;
import com.foodies.freshmeal.order.valueObject.OrderNotes;
import com.foodies.freshmeal.order.valueObject.OrderTimeline;
import com.foodies.freshmeal.order.valueObject.PaymentInfo;
import com.foodies.freshmeal.order.valueObject.PriceSummary;
import com.foodies.freshmeal.order.valueObject.RestaurantSnapshot;
import com.foodies.freshmeal.user.valueObject.AddressSnapshot;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ============================================================================
 * OrderEntity
 * ============================================================================
 *
 * Aggregate Root for the Order Domain.
 *
 * An Order represents a historical transaction between a customer and a
 * restaurant.
 *
 * The Order intentionally stores snapshot information for customer,
 * restaurant, address and food items so that historical order information
 * remains unchanged even when the corresponding master data changes later.
 *
 * ============================================================================
 *
 * Core Responsibilities
 * ---------------------
 *
 * - Identifies the order
 * - Stores customer snapshot
 * - Stores restaurant snapshot
 * - Stores delivery address snapshot
 * - Stores ordered food item snapshots
 * - Stores order pricing
 * - Stores payment information
 * - Stores delivery information
 * - Maintains order lifecycle
 * - Maintains cancellation information
 * - Maintains historical timestamps
 * - Stores order metadata
 * - Stores operational information required by Admin
 *
 * ============================================================================
 *
 * Future-ready capabilities
 * -------------------------
 *
 * The entity is intentionally designed to support future:
 *
 * - Consumer Panel
 * - Restaurant Panel
 * - Payment Gateway
 * - Delivery Management
 * - Scheduled Orders
 * - Gift Orders
 * - Invoicing
 * - Coupons
 * - Analytics
 *
 * without requiring a redesign of the Order aggregate.
 *
 * ============================================================================
 */
@Getter
@Setter
@Document(collection = "fm_orders")
@CompoundIndexes({

        /**
         * Useful for:
         *
         * Customer order history
         * Customer + status filtering
         */
        @CompoundIndex(name = "idx_order_customer_status", def = "{'customer.customerId':1,'orderStatus':1}"),

        /**
         * Useful for Admin order listing and filtering by status
         * while sorting by latest order.
         */
        @CompoundIndex(name = "idx_order_status_orderedAt", def = "{'orderStatus':1,'timeline.orderedAt':-1}"),

        /**
         * Useful for restaurant-side order management in the future.
         */
        @CompoundIndex(name = "idx_order_restaurant_status", def = "{'restaurant.restaurantId':1,'orderStatus':1}")

})
public class OrderEntity extends ABaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Constructor
    // =========================================================================

    /**
     * Package-private constructor.
     *
     * Entity creation should happen through the factory method.
     */
    OrderEntity() {
        // Intentionally package-private.
    }

    /**
     * Factory method for creating an OrderEntity.
     *
     * @return new OrderEntity instance
     */
    public static IEntity create() {
        return new OrderEntity();
    }

    // =========================================================================
    // Order Identification
    // =========================================================================

    /**
     * Business-facing unique order number.
     *
     * Example:
     *
     * FM202608140001
     *
     * This is different from the MongoDB document ID.
     */
    @Indexed(unique = true)
    @NotBlank
    private String orderNumber;

    // =========================================================================
    // Order Type
    // =========================================================================

    /**
     * Defines how the order is fulfilled.
     *
     * Examples:
     *
     * DELIVERY
     * TAKEAWAY
     * DINE_IN
     */
    @NotNull
    @Indexed
    private OrderTypeConstant orderType;

    // =========================================================================
    // Customer Snapshot
    // =========================================================================

    /**
     * Historical snapshot of the customer at order creation time.
     */
    @Valid
    @NotNull
    private CustomerSnapshot customer;

    // =========================================================================
    // Restaurant Snapshot
    // =========================================================================

    /**
     * Historical snapshot of the restaurant at order creation time.
     */
    @Valid
    @NotNull
    private RestaurantSnapshot restaurant;

    // =========================================================================
    // Delivery Address Snapshot
    // =========================================================================

    /**
     * Historical snapshot of the delivery address.
     *
     * This is intentionally embedded into the order instead of referencing
     * the customer's current address.
     */
    @Valid
    private AddressSnapshot deliveryAddress;

    // =========================================================================
    // Ordered Items
    // =========================================================================

    /**
     * Food items ordered by the customer.
     *
     * Each OrderItem contains a historical food snapshot and pricing
     * information at the time of order placement.
     */
    @Valid
    @NotEmpty
    private List<OrderItem> items = new ArrayList<>();

    // =========================================================================
    // Pricing
    // =========================================================================

    /**
     * Complete historical pricing breakdown.
     *
     * All monetary values are calculated by the backend and persisted as
     * part of the order.
     */
    @Valid
    @NotNull
    private PriceSummary priceSummary;

    // =========================================================================
    // Payment
    // =========================================================================

    /**
     * Payment lifecycle information.
     *
     * Includes payment status, gateway information and refund information.
     */
    @Valid
    private PaymentInfo paymentInfo;

    // =========================================================================
    // Delivery
    // =========================================================================

    /**
     * Current delivery status.
     *
     * This allows the Order domain to maintain the current delivery state
     * while detailed delivery functionality can be introduced later.
     */
    @Indexed
    private DeliveryStatusConstant deliveryStatus;

    /**
     * Delivery partner snapshot.
     *
     * Null until a delivery partner is assigned.
     */
    @Valid
    private DeliveryPartnerSnapshot deliveryPartner;

    // =========================================================================
    // Order Lifecycle
    // =========================================================================

    /**
     * Current business status of the order.
     *
     * Example:
     *
     * PLACED
     * CONFIRMED
     * PREPARING
     * READY
     * DELIVERED
     * CANCELLED
     * REJECTED
     */
    @Indexed
    @NotNull
    private OrderStatusConstant orderStatus = OrderStatusConstant.PLACED;

    // =========================================================================
    // Timeline
    // =========================================================================

    /**
     * Historical timestamps for important order lifecycle events.
     */
    @Valid
    private OrderTimeline timeline = new OrderTimeline();

    // =========================================================================
    // Cancellation
    // =========================================================================

    /**
     * Cancellation details.
     *
     * Null when the order has not been cancelled.
     */
    @Valid
    private CancellationInfo cancellationInfo;

    // =========================================================================
    // Notes
    // =========================================================================

    /**
     * Optional order-level notes.
     */
    @Valid
    private OrderNotes notes;

    // =========================================================================
    // Metadata
    // =========================================================================

    /**
     * Technical, marketing and source metadata.
     *
     * Examples:
     *
     * - WEB
     * - MOBILE_APP
     * - Campaign
     * - Referral source
     * - Device
     * - Application version
     */
    @Valid
    private OrderMetadata metadata;

    // =========================================================================
    // Order Statistics
    // =========================================================================

    /**
     * Number of distinct food items in the order.
     *
     * Example:
     *
     * Burger x 2
     * Pizza x 1
     * Coke x 1
     *
     * totalItems = 3
     */
    private Integer totalItems;

    /**
     * Total quantity across all ordered items.
     *
     * Example:
     *
     * Burger x 2
     * Pizza x 1
     * Coke x 3
     *
     * totalQuantity = 6
     */
    private Integer totalQuantity;

    // =========================================================================
    // Scheduled Order
    // =========================================================================

    /**
     * Indicates whether this is a scheduled order.
     */
    private Boolean scheduledOrder = Boolean.FALSE;

    /**
     * Scheduled delivery date and time.
     *
     * Null for normal immediate orders.
     */
    private LocalDateTime scheduledDeliveryAt;

    // =========================================================================
    // Gift Order
    // =========================================================================

    /**
     * Indicates whether this is a gift order.
     */
    private Boolean giftOrder = Boolean.FALSE;

    // =========================================================================
    // Invoice
    // =========================================================================

    /**
     * Indicates whether an invoice has been generated.
     */
    private Boolean invoiceGenerated = Boolean.FALSE;

    /**
     * Generated invoice number.
     */
    private String invoiceNumber;

    // =========================================================================
    // Tags
    // =========================================================================

    /**
     * Internal/system-generated tags.
     *
     * Examples:
     *
     * VIP
     * FIRST_ORDER
     * BULK_ORDER
     * HIGH_VALUE
     */
    @Field("tags")
    private List<String> tags = new ArrayList<>();

}