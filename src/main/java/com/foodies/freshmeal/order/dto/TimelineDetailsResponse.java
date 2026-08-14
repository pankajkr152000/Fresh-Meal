package com.foodies.freshmeal.order.dto;

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
 * TimelineDetailsResponse
 * ============================================================================
 *
 * Represents the complete order lifecycle timeline displayed on the Admin
 * Order Details page.
 *
 * This DTO is an API representation of OrderTimeline.
 *
 * Each timestamp represents when a particular business event occurred.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineDetailsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Order Lifecycle
    // =========================================================================

    /**
     * Time when the order was placed.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime orderedAt;

    /**
     * Time when the restaurant confirmed the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime confirmedAt;

    /**
     * Time when the restaurant rejected the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime rejectedAt;

    /**
     * Time when the kitchen started preparing the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime preparationStartedAt;

    /**
     * Time when the food became ready for pickup.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime readyForPickupAt;

    /**
     * Time when the delivery partner picked up the order.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime pickedUpAt;

    /**
     * Time when the delivery partner started delivery.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime outForDeliveryAt;

    /**
     * Time when the order was successfully delivered.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime deliveredAt;

    /**
     * Time when the order was cancelled.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime cancelledAt;

    /**
     * Estimated delivery time.
     */
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime estimatedDeliveryAt;

}