package com.foodies.freshmeal.order.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.order.dto.AddressDetailsResponse;
import com.foodies.freshmeal.order.dto.CancellationDetailsResponse;
import com.foodies.freshmeal.order.dto.CustomerDetailsResponse;
import com.foodies.freshmeal.order.dto.DeliveryPartnerDetailsResponse;
import com.foodies.freshmeal.order.dto.OrderDetailsResponse;
import com.foodies.freshmeal.order.dto.OrderItemDetailsResponse;
import com.foodies.freshmeal.order.dto.OrderListResponse;
import com.foodies.freshmeal.order.dto.OrderMetadataResponse;
import com.foodies.freshmeal.order.dto.OrderNotesResponse;
import com.foodies.freshmeal.order.dto.PaymentDetailsResponse;
import com.foodies.freshmeal.order.dto.PriceDetailsResponse;
import com.foodies.freshmeal.order.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.order.dto.TimelineDetailsResponse;
import com.foodies.freshmeal.order.entity.OrderEntity;
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

/**
 * ============================================================================
 * Component : OrderMapper
 * ============================================================================
 *
 * Responsible for converting the Order domain/persistence model into the
 * API response representations used by the FreshMeal application.
 *
 * <p>
 * The Order entity contains historical snapshots and domain value objects.
 * Those objects are intentionally not exposed directly through the REST API.
 * This mapper therefore acts as the boundary between the Order domain model
 * and the API contract.
 * </p>
 *
 * <p>
 * The Order module exposes two primary projections:
 * </p>
 *
 * <ul>
 * <li>{@link OrderListResponse} - lightweight Order List projection.</li>
 * <li>{@link OrderDetailsResponse} - complete Order Details projection.</li>
 * </ul>
 *
 * <p>
 * The mapper contains no business logic, repository access, request context,
 * validation logic or persistence operations.
 * </p>
 *
 * ============================================================================
 *
 * Mapping Direction
 * -----------------
 *
 * OrderEntity
 * |
 * +----> OrderListResponse
 * |
 * +----> OrderDetailsResponse
 *
 * ============================================================================
 *
 * Monetary Mapping
 * ---------------
 *
 * The persistence/domain model currently stores monetary amounts as
 * BigDecimal together with a currency field.
 *
 * The API model exposes monetary values through the common Money value object.
 *
 * Therefore this mapper is the boundary:
 *
 * BigDecimal + Currency
 * ↓
 * Money
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class OrderMapper {

    // =========================================================================
    // Order List
    // =========================================================================

    /**
     * Converts an OrderEntity into the lightweight Order List representation.
     *
     * @param entity Order entity.
     *
     * @return Order List response, or null when the entity is null.
     */
    public OrderListResponse toListResponse(
            final OrderEntity entity) {

        if (entity == null) {
            return null;
        }

        final PriceSummary priceSummary = entity.getPriceSummary();
        final PaymentInfo paymentInfo = entity.getPaymentInfo();
        final OrderTimeline timeline = entity.getTimeline();

        return OrderListResponse.builder()

                // -----------------------------------------------------------------
                // Identification
                // -----------------------------------------------------------------
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())

                // -----------------------------------------------------------------
                // Customer
                // -----------------------------------------------------------------
                .customer(toCustomerSummary(entity.getCustomer()))

                // -----------------------------------------------------------------
                // Restaurant
                // -----------------------------------------------------------------
                .restaurant(toRestaurantSummary(entity.getRestaurant()))

                // -----------------------------------------------------------------
                // Classification
                // -----------------------------------------------------------------
                .orderType(toDisplayOption(entity.getOrderType()))
                .orderStatus(toDisplayOption(entity.getOrderStatus()))
                .deliveryStatus(toDisplayOption(entity.getDeliveryStatus()))
                .paymentStatus(
                        paymentInfo == null
                                ? null
                                : toDisplayOption(paymentInfo.getPaymentStatus()))

                // -----------------------------------------------------------------
                // Statistics
                // -----------------------------------------------------------------
                .totalItems(entity.getTotalItems())
                .totalQuantity(entity.getTotalQuantity())

                // -----------------------------------------------------------------
                // Pricing
                // -----------------------------------------------------------------
                .grandTotal(
                        priceSummary == null
                                ? null
                                : toMoney(
                                        priceSummary.getGrandTotal(),
                                        priceSummary.getCurrency()))

                // -----------------------------------------------------------------
                // Timeline
                // -----------------------------------------------------------------
                .orderedAt(
                        timeline == null
                                ? null
                                : timeline.getOrderedAt())

                .build();
    }

    /**
     * Converts a list of OrderEntity objects into Order List responses.
     *
     * @param entities Order entities.
     *
     * @return mapped Order List responses.
     */
    public List<OrderListResponse> toListResponses(
            final List<OrderEntity> entities) {

        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .filter(entity -> entity != null)
                .map(this::toListResponse)
                .toList();
    }

    // =========================================================================
    // Order Details
    // =========================================================================

    /**
     * Converts an OrderEntity into the complete Order Details representation.
     *
     * @param entity Order entity.
     *
     * @return complete Order Details response, or null when entity is null.
     */
    public OrderDetailsResponse toDetailsResponse(
            final OrderEntity entity) {

        if (entity == null) {
            return null;
        }

        return OrderDetailsResponse.builder()

                // -----------------------------------------------------------------
                // Identification
                // -----------------------------------------------------------------
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())

                // -----------------------------------------------------------------
                // Classification
                // -----------------------------------------------------------------
                .orderType(toDisplayOption(entity.getOrderType()))
                .orderStatus(toDisplayOption(entity.getOrderStatus()))
                .deliveryStatus(toDisplayOption(entity.getDeliveryStatus()))

                // -----------------------------------------------------------------
                // Customer
                // -----------------------------------------------------------------
                .customer(toCustomerDetails(entity.getCustomer()))

                // -----------------------------------------------------------------
                // Restaurant
                // -----------------------------------------------------------------
                .restaurant(toRestaurantDetails(entity.getRestaurant()))

                // -----------------------------------------------------------------
                // Delivery Address
                // -----------------------------------------------------------------
                .deliveryAddress(
                        toAddressDetails(entity.getDeliveryAddress()))

                // -----------------------------------------------------------------
                // Items
                // -----------------------------------------------------------------
                .items(
                        toItemDetailsList(
                                entity.getItems(),
                                resolveCurrency(entity.getPriceSummary())))

                // -----------------------------------------------------------------
                // Pricing
                // -----------------------------------------------------------------
                .priceSummary(
                        toPriceDetails(entity.getPriceSummary()))

                // -----------------------------------------------------------------
                // Payment
                // -----------------------------------------------------------------
                .payment(
                        toPaymentDetails(entity.getPaymentInfo()))

                // -----------------------------------------------------------------
                // Delivery
                // -----------------------------------------------------------------
                .deliveryPartner(
                        toDeliveryPartnerDetails(
                                entity.getDeliveryPartner()))

                // -----------------------------------------------------------------
                // Timeline
                // -----------------------------------------------------------------
                .timeline(
                        toTimelineDetails(entity.getTimeline()))

                // -----------------------------------------------------------------
                // Cancellation
                // -----------------------------------------------------------------
                .cancellation(
                        toCancellationDetails(
                                entity.getCancellationInfo()))

                // -----------------------------------------------------------------
                // Notes
                // -----------------------------------------------------------------
                .notes(
                        toNotesResponse(entity.getNotes()))

                // -----------------------------------------------------------------
                // Metadata
                // -----------------------------------------------------------------
                .metadata(
                        toMetadataResponse(
                                entity.getMetadata(),
                                entity))

                // -----------------------------------------------------------------
                // Statistics
                // -----------------------------------------------------------------
                .totalItems(entity.getTotalItems())
                .totalQuantity(entity.getTotalQuantity())

                // -----------------------------------------------------------------
                // Scheduled Order
                // -----------------------------------------------------------------
                .scheduledOrder(entity.getScheduledOrder())
                .scheduledDeliveryAt(entity.getScheduledDeliveryAt())

                // -----------------------------------------------------------------
                // Gift Order
                // -----------------------------------------------------------------
                .giftOrder(entity.getGiftOrder())

                // -----------------------------------------------------------------
                // Invoice
                // -----------------------------------------------------------------
                .invoiceGenerated(entity.getInvoiceGenerated())
                .invoiceNumber(entity.getInvoiceNumber())

                // -----------------------------------------------------------------
                // Tags
                // -----------------------------------------------------------------
                .tags(
                        entity.getTags() == null
                                ? Collections.emptyList()
                                : List.copyOf(entity.getTags()))

                // -----------------------------------------------------------------
                // Audit
                // -----------------------------------------------------------------
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }

    // =========================================================================
    // Customer Mapping
    // =========================================================================

    /**
     * Maps the lightweight customer snapshot used by the Order List.
     *
     * @param source customer snapshot.
     *
     * @return customer summary.
     */
    private OrderListResponse.CustomerSummary toCustomerSummary(
            final CustomerSnapshot source) {

        if (source == null) {
            return null;
        }

        return OrderListResponse.CustomerSummary.builder()
                .customerId(source.getCustomerId())
                .customerName(source.getCustomerName())
                .build();
    }

    /**
     * Maps the complete customer snapshot.
     *
     * @param source customer snapshot.
     *
     * @return customer details response.
     */
    private CustomerDetailsResponse toCustomerDetails(
            final CustomerSnapshot source) {

        if (source == null) {
            return null;
        }

        return CustomerDetailsResponse.builder()
                .customerId(source.getCustomerId())
                .customerName(source.getCustomerName())
                .email(source.getEmail())
                .mobileNumber(source.getMobileNumber())
                .profileImageUrl(source.getProfileImageUrl())
                .guestCustomer(source.getGuestCustomer())
                .membershipId(source.getMembershipId())
                .membershipLevel(source.getMembershipLevel())
                .gstNumber(source.getGstNumber())
                .build();
    }

    // =========================================================================
    // Restaurant Mapping
    // =========================================================================

    /**
     * Maps the lightweight restaurant snapshot used by the Order List.
     *
     * @param source restaurant snapshot.
     *
     * @return restaurant summary.
     */
    private OrderListResponse.RestaurantSummary toRestaurantSummary(
            final RestaurantSnapshot source) {

        if (source == null) {
            return null;
        }

        return OrderListResponse.RestaurantSummary.builder()
                .restaurantId(source.getRestaurantId())
                .restaurantName(source.getRestaurantName())
                .build();
    }

    /**
     * Maps the complete restaurant snapshot.
     *
     * @param source restaurant snapshot.
     *
     * @return restaurant details response.
     */
    private RestaurantDetailsResponse toRestaurantDetails(
            final RestaurantSnapshot source) {

        if (source == null) {
            return null;
        }

        return RestaurantDetailsResponse.builder()
                .restaurantId(source.getRestaurantId())
                .restaurantCode(source.getRestaurantCode())
                .restaurantName(source.getRestaurantName())
                .emailAddress(source.getEmailAddress())
                .phoneNumber(source.getPhoneNumber())
                .address(source.getAddress())
                .geoLocation(source.getGeoLocation())
                .imageUrl(source.getImageUrl())
                .fssaiLicenseNumber(source.getFssaiLicenseNumber())
                .gstNumber(source.getGstNumber())
                .build();
    }

    // =========================================================================
    // Address Mapping
    // =========================================================================

    /**
     * Maps the historical delivery address snapshot.
     *
     * @param source address snapshot.
     *
     * @return address details response.
     */
    private AddressDetailsResponse toAddressDetails(
            final AddressSnapshot source) {

        if (source == null) {
            return null;
        }

        return AddressDetailsResponse.builder()
                .houseNumber(source.getHouseNumber())
                .apartmentName(source.getApartmentName())
                .street(source.getStreet())
                .area(source.getArea())
                .city(source.getCity())
                .district(source.getDistrict())
                .state(source.getState())
                .country(source.getCountry())
                .pincode(source.getPincode())
                .landmark(source.getLandmark())
                .contactPersonName(source.getContactPersonName())
                .contactMobileNumber(source.getContactMobileNumber())
                .deliveryInstruction(source.getDeliveryInstruction())
                .location(source.getLocation())
                .build();
    }

    // =========================================================================
    // Order Item Mapping
    // =========================================================================

    /**
     * Maps all OrderItem snapshots.
     *
     * @param sources order item snapshots.
     *
     * @return mapped item responses.
     */
    private List<OrderItemDetailsResponse> toItemDetailsList(
            final List<OrderItem> sources, final String currency) {

        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }

        return sources.stream()
                .filter(item -> item != null)
                .map(item -> this.toItemDetails(item, currency))
                .toList();
    }

    /**
     * Maps one OrderItem snapshot.
     *
     * <p>
     * Monetary values are converted into Money using the order currency.
     * The currency is resolved from the PriceSummary at the parent mapping
     * level and supplied here through the helper overload.
     * </p>
     *
     * @param source order item.
     *
     * @return item details response.
     */
    private OrderItemDetailsResponse toItemDetails(
            final OrderItem source, final String currency) {

        if (source == null) {
            return null;
        }

        /*
         * OrderItem currently contains raw monetary values but does not
         * contain its own currency field. FreshMeal therefore uses INR as
         * the default currency for item-level monetary snapshots.
         *
         * The final Order PriceSummary remains the authoritative order
         * currency.
         */
        return OrderItemDetailsResponse.builder()
                .foodId(source.getFoodId())
                .foodName(source.getFoodName())
                .description(source.getDescription())
                .imageUrl(source.getImageUrl())
                .foodCategory(source.getFoodCategory())
                .cuisineType(source.getCuisineType())
                .dietCategory(source.getDietCategory())
                .quantity(source.getQuantity())
                .unitPrice(toMoney(source.getUnitPrice(), currency))
                .discountAmount(toMoney(source.getDiscountAmount(), currency))
                .taxAmount(toMoney(source.getTaxAmount(), currency))
                .lineTotal(toMoney(source.getLineTotal(), currency))
                .specialInstruction(source.getSpecialInstruction())
                .build();
    }

    // =========================================================================
    // Price Mapping
    // =========================================================================

    /**
     * Maps the complete historical pricing summary.
     *
     * @param source price summary.
     *
     * @return price details response.
     */
    private PriceDetailsResponse toPriceDetails(
            final PriceSummary source) {

        if (source == null) {
            return null;
        }

        final String currency = source.getCurrency();

        return PriceDetailsResponse.builder()
                .itemTotal(toMoney(source.getItemTotal(), currency))
                .itemDiscount(toMoney(source.getItemDiscount(), currency))
                .couponDiscount(toMoney(source.getCouponDiscount(), currency))
                .couponCode(source.getCouponCode())
                .couponName(source.getCouponName())
                .taxAmount(toMoney(source.getTaxAmount(), currency))
                .cgst(toMoney(source.getCgst(), currency))
                .sgst(toMoney(source.getSgst(), currency))
                .deliveryCharge(
                        toMoney(source.getDeliveryCharge(), currency))
                .packingCharge(
                        toMoney(source.getPackingCharge(), currency))
                .platformFee(
                        toMoney(source.getPlatformFee(), currency))
                .tipAmount(
                        toMoney(source.getTipAmount(), currency))
                .roundOffAmount(
                        toMoney(source.getRoundOffAmount(), currency))
                .grandTotal(
                        toMoney(source.getGrandTotal(), currency))
                .build();
    }

    // =========================================================================
    // Payment Mapping
    // =========================================================================

    /**
     * Maps payment and refund information.
     *
     * @param source payment information.
     *
     * @return payment details response.
     */
    private PaymentDetailsResponse toPaymentDetails(
            final PaymentInfo source) {

        if (source == null) {
            return null;
        }

        return PaymentDetailsResponse.builder()

                // Payment
                .paymentMode(
                        toDisplayOption(source.getPaymentMode()))

                .paymentStatus(
                        toDisplayOption(source.getPaymentStatus()))

                .paymentGateway(source.getPaymentGateway())
                .gatewayOrderId(source.getGatewayOrderId())
                .transactionId(source.getTransactionId())
                .gatewayPaymentId(source.getGatewayPaymentId())
                .gatewayReferenceId(source.getGatewayReferenceId())
                .authorizationId(source.getAuthorizationId())

                .paidAmount(
                        toMoney(
                                source.getPaidAmount(),
                                source.getCurrency()))

                .paidAt(source.getPaidAt())
                .failureReason(source.getFailureReason())

                // Refund
                .refundStatus(
                        toDisplayOption(source.getRefundStatus()))

                .refundedAmount(
                        toMoney(
                                source.getRefundedAmount(),
                                source.getCurrency()))

                .refundTransactionId(
                        source.getRefundTransactionId())

                .refundReason(
                        source.getRefundReason())

                .refundedAt(
                        source.getRefundedAt())

                .build();
    }

    // =========================================================================
    // Delivery Partner Mapping
    // =========================================================================

    /**
     * Maps the delivery partner snapshot.
     *
     * @param source delivery partner snapshot.
     *
     * @return delivery partner details response.
     */
    private DeliveryPartnerDetailsResponse toDeliveryPartnerDetails(
            final DeliveryPartnerSnapshot source) {

        if (source == null) {
            return null;
        }

        return DeliveryPartnerDetailsResponse.builder()
                .deliveryPartnerId(source.getDeliveryPartnerId())
                .partnerCode(source.getPartnerCode())
                .partnerName(source.getPartnerName())
                .emailAddress(source.getEmailAddress())
                .phoneNumber(source.getPhoneNumber())
                .profileImageUrl(source.getProfileImageUrl())
                .vehicleNumber(source.getVehicleNumber())
                .vehicleType(source.getVehicleType())
                .build();
    }

    // =========================================================================
    // Timeline Mapping
    // =========================================================================

    /**
     * Maps the complete Order timeline.
     *
     * @param source order timeline.
     *
     * @return timeline response.
     */
    private TimelineDetailsResponse toTimelineDetails(
            final OrderTimeline source) {

        if (source == null) {
            return null;
        }

        return TimelineDetailsResponse.builder()
                .orderedAt(source.getOrderedAt())
                .confirmedAt(source.getConfirmedAt())
                .rejectedAt(source.getRejectedAt())
                .preparationStartedAt(
                        source.getPreparationStartedAt())
                .readyForPickupAt(
                        source.getReadyForPickupAt())
                .pickedUpAt(source.getPickedUpAt())
                .outForDeliveryAt(
                        source.getOutForDeliveryAt())
                .deliveredAt(source.getDeliveredAt())
                .cancelledAt(source.getCancelledAt())
                .estimatedDeliveryAt(
                        source.getEstimatedDeliveryAt())
                .build();
    }

    // =========================================================================
    // Cancellation Mapping
    // =========================================================================

    /**
     * Maps cancellation information.
     *
     * @param source cancellation information.
     *
     * @return cancellation response.
     */
    private CancellationDetailsResponse toCancellationDetails(
            final CancellationInfo source) {

        if (source == null) {
            return null;
        }

        return CancellationDetailsResponse.builder()
                .cancelled(source.getCancelled())
                .cancelledAt(source.getCancelledAt())
                .cancelledBy(source.getCancelledBy())
                .cancellationSource(
                        toDisplayOption(
                                source.getCancellationSource()))
                .cancellationReason(
                        toDisplayOption(
                                source.getCancellationReason()))
                .cancellationRemarks(
                        source.getCancellationRemarks())
                .internalRemarks(
                        source.getInternalRemarks())
                .build();
    }

    // =========================================================================
    // Notes Mapping
    // =========================================================================

    /**
     * Maps order-level notes.
     *
     * @param source order notes.
     *
     * @return notes response.
     */
    private OrderNotesResponse toNotesResponse(
            final OrderNotes source) {

        if (source == null) {
            return null;
        }

        return OrderNotesResponse.builder()
                .customerNote(source.getCustomerNote())
                .restaurantNote(source.getRestaurantNote())
                .internalNote(source.getInternalNote())
                .build();
    }

    // =========================================================================
    // Metadata Mapping
    // =========================================================================

    /**
     * Maps order metadata.
     *
     * <p>
     * Coupon, gift and scheduling information intentionally remain owned by
     * their respective domain areas. The API metadata response contains those
     * values for convenient administrative display, so they are supplied from
     * the Order aggregate where appropriate.
     * </p>
     *
     * @param source order metadata.
     * @param entity order aggregate.
     *
     * @return metadata response.
     */
    private OrderMetadataResponse toMetadataResponse(
            final OrderMetadata source,
            final OrderEntity entity) {

        if (source == null && entity == null) {
            return null;
        }

        final PriceSummary priceSummary = entity == null
                ? null
                : entity.getPriceSummary();

        return OrderMetadataResponse.builder()

                .orderSource(
                        source == null
                                ? null
                                : toDisplayOption(
                                        source.getOrderSource()))

                .platformType(
                        source == null
                                ? null
                                : toDisplayOption(
                                        source.getPlatformType()))

                .deviceType(
                        source == null
                                ? null
                                : toDisplayOption(
                                        source.getDeviceType()))

                .applicationVersion(
                        source == null
                                ? null
                                : source.getApplicationVersion())

                .ipAddress(
                        source == null
                                ? null
                                : source.getIpAddress())

                .userAgent(
                        source == null
                                ? null
                                : source.getUserAgent())

                .campaignCode(
                        source == null
                                ? null
                                : source.getCampaignCode())

                .referralSource(
                        source == null
                                ? null
                                : source.getReferralSource())

                .couponCode(
                        priceSummary == null
                                ? null
                                : priceSummary.getCouponCode())

                .giftOrder(
                        entity == null
                                ? null
                                : entity.getGiftOrder())

                .scheduledOrder(
                        entity == null
                                ? null
                                : entity.getScheduledOrder())

                .build();
    }

    // =========================================================================
    // Display Option Mapping
    // =========================================================================

    /**
     * Converts an IDisplayOption implementation into the API-facing
     * DisplayOptionResponse.
     *
     * @param source display option.
     *
     * @return display option response.
     */
    private DisplayOptionResponse toDisplayOption(
            final IDisplayOption source) {

        if (source == null) {
            return null;
        }

        return new DisplayOptionResponse(
                source.getLabel(),
                source.getValue());
    }

    // =========================================================================
    // Money Mapping
    // =========================================================================

    /**
     * Converts a raw monetary amount and currency into the common Money
     * value object used by the API layer.
     *
     * @param amount   monetary amount.
     * @param currency ISO currency code.
     *
     * @return Money value object.
     */
    private Money toMoney(
            final BigDecimal amount,
            final String currency) {

        if (amount == null) {
            return null;
        }

        return Money.builder()
                .amount(amount)
                .currency(
                        currency == null || currency.isBlank()
                                ? "INR"
                                : currency)
                .build();
    }

    /**
     * Resolves the currency from a PriceSummary.
     *
     * @param priceSummary price summary.
     *
     * @return currency code, or "INR" if null.
     */
    private String resolveCurrency(
            final PriceSummary priceSummary) {

        if (priceSummary == null) {
            return "INR";
        }

        final String currency = priceSummary.getCurrency();
        return currency == null || currency.isBlank()
                ? "INR"
                : currency;
    }

}