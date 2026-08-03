package com.foodies.freshmeal.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.order.constants.OrderStatusConstant;
import com.foodies.freshmeal.order.valueObject.AddressSnapshot;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
 * Responsibilities:
 * - Stores complete order information
 * - Maintains historical snapshots
 * - Maintains pricing information
 * - Maintains payment information
 * - Maintains delivery information
 * - Maintains lifecycle timestamps
 *
 * NOTE:
 * This entity intentionally stores snapshot data instead of only IDs so that
 * historical orders remain unchanged even if customer, restaurant or food data
 * changes in future.
 *
 * Inherits:
 * - createdAt
 * - createdBy
 * - updatedAt
 * - updatedBy
 * - deletedFlag
 * - deletedAt
 * - deletedBy
 * - recordStatus
 *
 * from ABaseEntity.
 * ============================================================================
 */
@Getter
@Setter
@Document(collection = "fm_orders")
@CompoundIndexes({

        @CompoundIndex(name = "idx_order_number", def = "{'orderNumber':1}", unique = true),

        @CompoundIndex(name = "idx_customer_status", def = "{'customer.customerId':1,'orderStatus':1}"),

        @CompoundIndex(name = "idx_status_orderedAt", def = "{'orderStatus':1,'timeline.orderedAt':-1}")

})
public class OrderEntity extends ABaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * =====================================================
     * Constructor
     * =====================================================
     *
     * Package-private constructor.
     * Entity creation should happen only through factory.
     *
     * =====================================================
     */
    OrderEntity() {
        // Package-private constructor.
        // Entity creation should happen only through factory.
    }

    /**
     * =====================================================
     * Factory Method
     * =====================================================
     */
    public static IEntity create() {
        return new OrderEntity();
    }

    // -------------------------------------------------------------------------
    // MongoDB
    // -------------------------------------------------------------------------

    @Id
    private String id;

    /**
     * Optimistic locking.
     */
    @Version
    private Long version;

    // -------------------------------------------------------------------------
    // Order Identification
    // -------------------------------------------------------------------------

    /**
     * Example:
     * FM202608030001
     */
    @Indexed(unique = true)
    @NotBlank
    private String orderNumber;

    /**
     * Public reference number shown to customers if needed.
     */
    @Indexed
    private String referenceNumber;

    // -------------------------------------------------------------------------
    // Customer Information
    // -------------------------------------------------------------------------

    @Valid
    @NotNull
    private CustomerSnapshot customer;

    // -------------------------------------------------------------------------
    // Restaurant Information
    // -------------------------------------------------------------------------

    @Valid
    @NotNull
    private RestaurantSnapshot restaurant;

    // -------------------------------------------------------------------------
    // Delivery Address
    // -------------------------------------------------------------------------

    @Valid
    @NotNull
    private AddressSnapshot deliveryAddress;

    // -------------------------------------------------------------------------
    // Ordered Items
    // -------------------------------------------------------------------------

    @Valid
    @NotNull
    private List<OrderItem> items = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Pricing
    // -------------------------------------------------------------------------

    @Valid
    @NotNull
    private PriceSummary priceSummary;

    // -------------------------------------------------------------------------
    // Payment
    // -------------------------------------------------------------------------

    @Valid
    private PaymentInfo paymentInfo;

    // -------------------------------------------------------------------------
    // Delivery Partner
    // -------------------------------------------------------------------------

    @Valid
    private DeliveryPartnerSnapshot deliveryPartner;

    // -------------------------------------------------------------------------
    // Order Status
    // -------------------------------------------------------------------------

    @Indexed
    @NotNull
    private OrderStatusConstant orderStatus = OrderStatusConstant.PLACED;

    // -------------------------------------------------------------------------
    // Timeline
    // -------------------------------------------------------------------------

    @Valid
    private OrderTimeline timeline = new OrderTimeline();

    // -------------------------------------------------------------------------
    // Cancellation
    // -------------------------------------------------------------------------

    @Valid
    private CancellationInfo cancellationInfo;

    // -------------------------------------------------------------------------
    // Notes
    // -------------------------------------------------------------------------

    @Valid
    private OrderNotes notes = new OrderNotes();

    // -------------------------------------------------------------------------
    // Metadata
    // -------------------------------------------------------------------------

    @Valid
    private OrderMetadata metadata = new OrderMetadata();

    // -------------------------------------------------------------------------
    // Additional Information
    // -------------------------------------------------------------------------

    /**
     * Number of items in the order.
     * Useful for analytics and dashboards.
     */
    private Integer totalItems;

    /**
     * Total quantity across all ordered items.
     */
    private Integer totalQuantity;

    /**
     * Indicates whether this is a scheduled order.
     */
    private Boolean scheduledOrder = Boolean.FALSE;

    /**
     * Scheduled delivery date and time.
     */
    private LocalDateTime scheduledDeliveryAt;

    /**
     * Indicates gift order.
     */
    private Boolean giftOrder = Boolean.FALSE;

    /**
     * High priority order.
     */
    private Boolean priorityOrder = Boolean.FALSE;

    /**
     * Whether invoice has been generated.
     */
    private Boolean invoiceGenerated = Boolean.FALSE;

    /**
     * Invoice number.
     */
    private String invoiceNumber;

    /**
     * Internal tags.
     * Example:
     * VIP
     * FIRST_ORDER
     * BULK_ORDER
     */
    @Field("tags")
    private List<String> tags = new ArrayList<>();

}
