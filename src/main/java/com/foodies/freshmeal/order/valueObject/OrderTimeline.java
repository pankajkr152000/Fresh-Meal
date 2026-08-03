package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.date.DateConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderTimeline
 * ============================================================================
 *
 * Represents the complete lifecycle timeline of an order.
 *
 * This value object captures all important timestamps from order placement
 * until completion or cancellation.
 *
 * ============================================================================
 *
 * Order Flow
 *
 * Order Placed
 * │
 * ▼
 * Order Confirmed
 * │
 * ▼
 * Preparation Started
 * │
 * ▼
 * Ready For Pickup
 * │
 * ▼
 * Picked Up
 * │
 * ▼
 * Out For Delivery
 * │
 * ▼
 * Delivered
 *
 * OR
 *
 * Cancelled
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
public class OrderTimeline implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Order placement time.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime orderedAt;

    /**
     * Restaurant accepted the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime confirmedAt;

    /**
     * Kitchen started preparing the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime preparationStartedAt;

    /**
     * Food is ready for pickup.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime readyForPickupAt;

    /**
     * Delivery partner picked up the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime pickedUpAt;

    /**
     * Delivery partner is on the way.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime outForDeliveryAt;

    /**
     * Order delivered successfully.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime deliveredAt;

    /**
     * Order cancelled.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime cancelledAt;

    /**
     * Estimated delivery time.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime estimatedDeliveryAt;

}